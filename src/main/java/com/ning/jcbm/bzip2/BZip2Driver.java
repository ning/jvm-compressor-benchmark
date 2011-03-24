package com.ning.jcbm.bzip2;

import java.io.*;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import com.ning.jcbm.DriverBase;

public class BZip2Driver  extends DriverBase
{
    public BZip2Driver() {
        super("bzip2");
    }

    // No native Block API; but need some impl for test framework
    
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        return uncompressBlockUsingStream(new BZip2CompressorInputStream(new ByteArrayInputStream(compressed)));
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        BZip2CompressorOutputStream out = new BZip2CompressorOutputStream(rawOut);
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
