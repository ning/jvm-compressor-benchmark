package com.ning.jcbm.bzip2;

import java.io.*;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import com.ning.jcbm.DriverBase;

public class BZip2Driver  extends DriverBase
{
    private final static int COMP_LEVEL = 2; // from 1 to 9; 100 - 900k blocks
    
    public BZip2Driver() {
        super("bzip2");
    }

    // No native Block API; but need some impl for test framework
    
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException {
        return compressBlockUsingStream(uncompressed, compressBuffer);
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException {
        return uncompressBlockUsingStream(new BZip2CompressorInputStream(new ByteArrayInputStream(compressed)), uncompressBuffer);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        BZip2CompressorOutputStream out = new BZip2CompressorOutputStream(rawOut, COMP_LEVEL);
        out.write(uncompressed);
        out.close();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        BZip2CompressorInputStream in = new BZip2CompressorInputStream(compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        return total;
    }
}
