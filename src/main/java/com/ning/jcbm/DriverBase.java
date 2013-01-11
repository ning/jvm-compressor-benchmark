package com.ning.jcbm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.ning.jcbm.util.DevNullOutputStream;
import com.sun.japex.Constants;
import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;

/**
 */
public abstract class DriverBase extends JapexDriverBase
{
    /* 24-Mar-2011, tatu: Looks like we get skewed values for some things; specifically,
     *    comp/uncomp speeds in gigabytes. This is problematic mostly just for graphs,
     *    since outliers stretch stuff. So, let's just cap to some max value.
     *    
     *    For compression, 0.5 gig/sec could serve as boundary (highest so far seen was 2.5G/s);
     *    no compressible test case has produced such speeds for compression.
     *    For decompression, fastest codecs can legitimately expand at bit higher speeds,
     *    so let's upgrade this to 0.6 gig/sec for now
     */
    
    final static double MAX_COMPRESS_THROUGHPUT = 599.9;

    final static double MAX_UNCOMPRESS_THROUGHPUT = 599.9;

    final static double MAX_BOTH_THROUGHPUT = 399.9;
    
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
     * The buffer where to compress data.
     */
    protected byte[] _compressBuffer;

    /**
     * Compressed test data
     */
    protected byte[] _compressed;

    /**
     * The buffer where to uncompress data.
     */
    protected byte[] _uncompressBuffer;

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
        } else if (operStr.startsWith("R")) {
            _operation = Operation.ROUNDTRIP;            
        } else {
            throw new IllegalArgumentException("Invalid 'operation' part of name, '"+operStr+"': should start with 'C', 'U' or 'R'");
        }
        _inputFile = new File(_inputDir, filename);
        try {
            // First things first: load uncompressed input in memory; compress to verify round-tripping
            _uncompressed = loadFile(_inputFile);
            _compressed = new byte[maxCompressedLength(_uncompressed.length)];
            int compressedLen = compressBlock(_uncompressed, _compressed);
            _compressed = Arrays.copyOf(_compressed, compressedLen);
            _compressBuffer = new byte[maxCompressedLength(_uncompressed.length)];
            // +1024 so that it can't be used by the driver to find the original length
            _uncompressBuffer = new byte[_uncompressed.length + 1024];
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
            switch (_operation) {
            case COMPRESS:
                if (_streaming) {
                    DevNullOutputStream out = new DevNullOutputStream();
                    compressToStream(_uncompressed, out);
                    out.close();
                    _totalLength = out.length();
                } else {
                    _totalLength = compressBlock(_uncompressed, _compressBuffer);
                }
                break;
            case UNCOMPRESS:
                if (_streaming) {
                    ByteArrayInputStream in = new ByteArrayInputStream(_compressed);
                    _totalLength = uncompressFromStream(in, _inputBuffer);
                    in.close();
                } else {
                    _totalLength = uncompressBlock(_compressed, _uncompressBuffer);
                }
                break;
            default:
                if (_streaming) { // just use bogus stream here too
                    DevNullOutputStream out = new DevNullOutputStream();
                    compressToStream(_uncompressed, out);
                    out.close();
                    ByteArrayInputStream in = new ByteArrayInputStream(_compressed);
                    _totalLength = uncompressFromStream(in, _inputBuffer);
                    in.close();
                } else {
                    compressBlock(_uncompressed, _compressBuffer);
                    _totalLength = uncompressBlock(_compressed, _uncompressBuffer);
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
        double MAX;
        if (_operation == Operation.COMPRESS) {
            MAX = MAX_COMPRESS_THROUGHPUT;
        } else if (_operation == Operation.UNCOMPRESS) {
            MAX = MAX_UNCOMPRESS_THROUGHPUT;
        } else {
            MAX = MAX_BOTH_THROUGHPUT;
        }
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

    protected int maxCompressedLength(int length) {
        // Drivers that can compute the max compressed length should override
        // this method
        // Larg-ish default impl: 2*len + 1KB
        return 2 * length + 1024;
    }

    protected abstract int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException;

    protected abstract int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException;

    protected abstract void compressToStream(byte[] uncompressed, OutputStream out)
        throws IOException;

    protected abstract int uncompressFromStream(InputStream in, byte[] buffer)
        throws IOException;
    
    /*
    ///////////////////////////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////////////////////////
     */

    protected int compressBlockUsingStream(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(compressBuffer);
        compressToStream(uncompressed, out);
        return out.length();
    }

    protected int uncompressBlockUsingStream(InputStream in, byte[] uncompressBuffer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(uncompressBuffer);
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
        return out.length();
    }
    
    protected void verifyRoundTrip(String name, byte[] raw, byte[] compressed) throws IOException
    {
        byte[] actual = new byte[raw.length + 1];
        final int actualLength = uncompressBlock(compressed, actual);
        if (actualLength != raw.length) {
            throw new IllegalArgumentException("Round-trip failed for driver '"+_driverName+"', input '"+name+"': uncompressed length was "+actual.length+" bytes; expected "+raw.length);
        }
        int i = 0;
        for (int len = raw.length; i < len; ++i) {
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
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream((int) file.length());
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
