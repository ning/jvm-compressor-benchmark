package com.ning.jcbm;

import java.io.*;
import java.util.*;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

import com.ning.jcbm.util.*;

/**
 */
public abstract class DriverBase extends JapexDriverBase
{
    /**
     * Operation we are testing
     */
    protected Operation _operation;

    /**
     * Number of bytes processed during test
     */
    protected int _totalLength;

    /**
     * Depending on whether we want to consider file I/O overhead
     * or not, we can define temporary input/output files too
     */
    private File _tmpInputFile;

    private File _tmpOutputFile;    
    
    /**
     * In-memory input data used for read and read-write tests (possibly
     * written to a File).
     */
    protected byte[] _inputData;
//    protected List<byte[]> _inputData;

    @Override
    public void prepare(TestCase testCase)
    {
        String inputFile = testCase.getParam("japex.inputFile");        
        if (inputFile == null) {
            throw new RuntimeException("japex.inputFile not specified");
        }
        String tmpDirName = testCase.getParam("japex.tmpFileDir");        
        if (tmpDirName == null) {
            throw new RuntimeException("japex.tmpFileDir not specified");
        }
        tmpDirName = tmpDirName.trim();
        if (tmpDirName.length() == 0) { // no file i/o
            _tmpInputFile = null;
            _tmpOutputFile = null;
        } else { // use file i/o
            File dir = new File(tmpDirName);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new IllegalArgumentException("Could not create directory '"+dir+"': can not create temporary files");
                }
            }
            /*
            _tmpInputFile = new File(dir, TMP_INPUT_FILENAME);
            _tmpOutputFile = new File(dir, TMP_OUTPUT_FILENAME);
            */
        }
        _operation = null;
        String operStr = testCase.getParam("japex.operation");
        if (operStr != null) {
            try {
                _operation = Operation.valueOf(operStr);
            } catch (Exception e) { }
        }
        if (_operation == null) {
           throw new IllegalArgumentException("Invalid or missing value for japex.itemOperation (value: ["+operStr
                +"]), has to be one of: "+Arrays.asList(Operation.values()));
        }
        
        // First things first: load in input data, bind to value objects
        try {
            byte[] json = loadFile(inputFile);
//            _valuesToWrite = readJsonAs(json, _valueType);

            // And for read, read/write cases, convert to format we need
            
            // First: handle input data conversion to raw format if testing reading
            switch (_operation) {
            case READ:
            case READ_WRITE:
//                byte[] data = convertInputData(_valuesToWrite);
                byte[] data = null;

                // compression to use?
                _inputData = data;
                
                // And, with file-backed tests, write the thing
                if (_tmpInputFile != null) {
                    FileOutputStream out = new FileOutputStream(_tmpInputFile);
                    out.write(_inputData);
                    out.close();
                }
                break;
            case WRITE:
                _inputData = null;
            }
            // And for output tests, delete output file
            switch (_operation) {
            case READ_WRITE:
            case WRITE:
                if (_tmpOutputFile != null && _tmpOutputFile.exists()) {
                    _tmpOutputFile.delete();
                }
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void warmup(TestCase testCase) {
        run(testCase);
    }

    @Override
    public void run(TestCase testCase)
    {
        _totalLength = 0;

        try {
            _totalLength = runTest(_operation);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }   
    @Override
    public void finish(TestCase testCase)
    {
        // Set file size in KB on X axis
        testCase.setDoubleParam("japex.resultValueX", ((double) _totalLength) / 1024.0);
        getTestSuite().setParam("japex.resultUnitX", "KB");

        // Throughput choices; Mbsp, tps; for us 'tps' makes more sense due to differing sizes:
        getTestSuite().setParam("japex.resultUnit", "tps");
        //getTestSuite().setParam("japex.resultUnit", "mbps");

        if (_tmpInputFile != null && _tmpInputFile.exists()) {
            _tmpInputFile.delete();
        }
        if (_tmpOutputFile != null && _tmpOutputFile.exists()) {
            _tmpOutputFile.delete();
        }
    }

    /*
    ///////////////////////////////////////////////////////////////////////
    // Abstract methods for sub-classes to implement
    ///////////////////////////////////////////////////////////////////////
     */
    
    /**
     * @return Number of bytes processed (read and/or written) during the test
     */
    protected final int runTest(Operation operation) throws Exception
    {
        int totalBytes = 0;

        InputStream in = null;

        if (operation == Operation.READ || operation == Operation.READ_WRITE) {
            if (_tmpInputFile == null) {
                in = new ByteArrayInputStream(_inputData);
            } else {
                in = new FileInputStream(_tmpInputFile);
            }
        }

        int count;
        
        switch (operation) {
        case READ:
            count = runReadTest(in);
            totalBytes += _inputData.length;
            if (count != _valuesToWrite.size()) {
                throw new IllegalStateException("Failed to read entries: expected "+_valuesToWrite.size()+", found "+count);
            }
            break;
        case READ_WRITE:
            totalBytes += _inputData.length;
            if (_tmpOutputFile == null) {
                DevNullOutputStream nout = new DevNullOutputStream();
                OutputStream out = getCompressedStream(compression, nout);
                count = runReadWriteTest(in, out);
                out.close();
                totalBytes += nout.length();
            } else {
                FileOutputStream fout = new FileOutputStream(_tmpOutputFile);
                OutputStream out = fout;
                count = runReadWriteTest(in, out);
                fout.close();
                totalBytes += (int) _tmpOutputFile.length();
                _tmpOutputFile.delete();
            }
            if (count != _valuesToWrite.size()) {
                throw new IllegalStateException("Failed to read entries: expected "+_valuesToWrite.size()+", found "+count);
            }
            break;
        case WRITE:
            if (_tmpOutputFile == null) {
                DevNullOutputStream out = new DevNullOutputStream();
                runWriteTest(_valuesToWrite, out);
                out.close();
                totalBytes += out.length();
            } else {
                FileOutputStream out = new FileOutputStream(_tmpOutputFile);
                runWriteTest(_valuesToWrite, out);
                out.close();
                totalBytes += (int) _tmpOutputFile.length();
                _tmpOutputFile.delete();
            }
            break;
        default:
            throw new Error("Internal error");
        }

        return totalBytes;
    }

    protected abstract byte[] convertInputData(byte[] data) throws Exception;
    
    /**
     * @return Number of values read
     */
    protected abstract int runReadTest(InputStream in) throws Exception;
    
    /**
     * @return Number of values read
     */
    protected abstract int runReadWriteTest(InputStream in, OutputStream out) throws Exception;

    protected abstract void runWriteTest(byte[] data, OutputStream out) throws Exception;
    
    /*
    ///////////////////////////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////////////////////////
     */
    
    protected byte[] loadFile(String filename) throws IOException
    {
        File f = new File(filename);
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) f.length());
        byte[] buffer = new byte[4000];
        int count;
        
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
}
