package com.ning.jcbm;

import java.io.*;

import com.sun.japex.Constants;
import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

import com.ning.jcbm.util.*;

/**
 */
public abstract class DriverBase extends JapexDriverBase
{
    /* 24-Mar-2011, tatu: Looks like we get skewed values for some things; specifically,
     *    comp/uncomp speeds in gigabytes. This is problematic mostly just for graphs,
     *    since outliers stretch stuff. So, let's just cap to some max value.
     *    
     *    For now, 1gig/sec can serve as boundary (highest so far seen was 2.5G/s)
     */
    
    final static double MAX_COMPRESS_THROUGHPUT = 999.9;

    final static double MAX_UNCOMPRESS_THROUGHPUT = 999.9;
    
    protected final String _driverName;
    
    /**
     * Operation we are testing
     */
    protected Operation _operation;

    /**
     * Whether driver should use streaming compression or not (block-based)
     */
    protected boolean _streaming;
    
    /**
     * We will give a reusable input buffer for compressors to
     * give each a fair chance for reuse as needed.
     */
    protected final byte[] _inputBuffer = new byte[4000];
    
    /**
     * Number of bytes processed during test
     */
    protected int _totalLength;
    
    /**
     * Directory in which input files are stored (file names
     * are same as test case names)
     */
    private File _inputDir;

    private File _inputFile;
    
    /**
     * Uncompressed test data
     */
    protected byte[] _uncompressed;

    /**
     * Compressed test data
     */
    protected byte[] _compressed;

    protected DriverBase(String name)
    {
        _driverName = name;
    }

    /*
    ///////////////////////////////////////////////////////////////////////
    // Overridden driver methods
    ///////////////////////////////////////////////////////////////////////
     */
    
    @Override
    public void initializeDriver()
    {
        // Where are the input files?
        String dirName = getParam("japex.inputDir");
        if (dirName == null) {
            throw new RuntimeException("japex.inputFile not specified");
        }
        _inputDir = new File(dirName);
        if (!_inputDir.exists() || !_inputDir.isDirectory()) {
            throw new IllegalArgumentException("No input directory '"+_inputDir.getAbsolutePath()+"'");
        }
        _streaming = getBooleanParam("streaming");
    }
    
    @Override
    public void prepare(TestCase testCase)
    {
        String name = testCase.getName();

        
        String[] parts = name.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid test name '"+name+"'; should have at least 2 components separated by slash");
        }
        _operation = null;
        String operStr = parts[0];
        String filename = parts[1];
        if (operStr.startsWith("C")) {
            _operation = Operation.COMPRESS;
        } else if (operStr.startsWith("U")) {
            _operation = Operation.UNCOMPRESS;            
        } else {
            throw new IllegalArgumentException("Invalid 'operation' part of name, '"+operStr+"': should start with 'C' or 'U'");
        }
        _inputFile = new File(_inputDir, filename); 
        try {
            // First things first: load uncompressed input in memory; compress to verify round-tripping
            _uncompressed = loadFile(_inputFile);
            _compressed = compressBlock(_uncompressed);
            verifyRoundTrip(testCase.getName(), _uncompressed, _compressed);
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
            if (_operation == Operation.COMPRESS) {
                if (_streaming) {
                    DevNullOutputStream out = new DevNullOutputStream();
                    compressToStream(_uncompressed, out);
                    out.close();
                    _totalLength = out.length();
                } else {
                    byte[] stuff = compressBlock(_uncompressed);
                    _totalLength = stuff.length;
                }
            } else { // uncompress
                if (_streaming) {
                    ByteArrayInputStream in = new ByteArrayInputStream(_compressed);
                    _totalLength = uncompressFromStream(in, _inputBuffer);
                    in.close();
                } else {
                    byte[] stuff = uncompressBlock(_compressed);
                    _totalLength = stuff.length;
                }
            }
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
        // Set relative compressed size (in percents) as resultX
        double sizeRatio = calcSizeRatio();
        
        testCase.setDoubleParam("japex.resultValueX", 100.0 * sizeRatio);
        getTestSuite().setParam("japex.resultUnitX", "Size%");

        // And main result throughput, MB/s
        
//        testCase.setParam("japex.inputFile", _inputFile.getAbsolutePath());

        double itersPerSec = 1000.0 * testCase.getDoubleParam(Constants.RUN_ITERATIONS_SUM) / testCase.getDoubleParam(Constants.ACTUAL_RUN_TIME);
        double throughputMBps = itersPerSec * _uncompressed.length / (1024.0 * 1024.0);

        // truncate outliers (see javadocs for MAX values)
        double MAX = (_operation == Operation.COMPRESS) ? MAX_COMPRESS_THROUGHPUT : MAX_UNCOMPRESS_THROUGHPUT;
        if (throughputMBps > MAX) {
            throughputMBps = MAX;
        }
        
        testCase.setDoubleParam("japex.resultValue", throughputMBps);
        testCase.setParam("japex.resultUnit", "MB/s");
    }

    protected double calcSizeRatio() {
        return (double) _compressed.length / (double) _uncompressed.length;
    }
    
    /*
    ///////////////////////////////////////////////////////////////////////
    // Abstract methods for sub-classes to implement
    ///////////////////////////////////////////////////////////////////////
     */

    protected abstract byte[] compressBlock(byte[] uncompressed) throws IOException;

    protected abstract byte[] uncompressBlock(byte[] compressed) throws IOException;

    protected abstract void compressToStream(byte[] uncompressed, OutputStream out)
        throws IOException;

    protected abstract int uncompressFromStream(InputStream in, byte[] buffer)
        throws IOException;
    
    /*
    ///////////////////////////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////////////////////////
     */

    protected byte[] compressBlockUsingStream(byte[] uncompressed) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(uncompressed.length);
        compressToStream(uncompressed, out);
        return out.toByteArray();
    }

    protected byte[] uncompressBlockUsingStream(InputStream in) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(_compressed.length);
        byte[] buffer = new byte[4000];
        int count;
        while ((count = in.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
    
    protected void verifyRoundTrip(String name, byte[] raw, byte[] compressed) throws IOException
    {
        byte[] actual = uncompressBlock(compressed);
        if (actual.length != raw.length) {
            throw new IllegalArgumentException("Round-trip failed for driver '"+_driverName+"', input '"+name+"': uncompressed length was "+actual.length+" bytes; expected "+raw.length);
        }
        int i = 0;
        for (int len = actual.length; i < len; ++i) {
            if (actual[i] != raw[i]) {
                throw new IllegalArgumentException("Round-trip failed for driver '"+_driverName+"', input '"+name+"': bytes at offset "+i
                        +" (from total of "+len+") differed, expected 0x"+Integer.toHexString(raw[i] & 0xFF)
                        +", got 0x"+Integer.toHexString(actual[i] & 0xFF));
            }
        }
    }
    
    protected byte[] loadFile(File file) throws IOException
    {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());
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
