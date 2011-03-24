package com.ning.jcbm.gzip;

import java.io.*;

import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;

import com.ning.jcbm.DriverBase;

public class JCraftGzipDriver extends DriverBase
{
    // Which compression level should we use? Multiple? Start with half-way
    private final static int COMP_LEVEL = 5;
    
    public JCraftGzipDriver() {
        super("GZIP(jcraft)");
    }

    // No native Block API; but need some impl for test framework
    
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        return uncompressBlockUsingStream(compressed);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        ZOutputStream out = new ZOutputStream(rawOut, COMP_LEVEL);
        out.write(uncompressed);
        out.close();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        ZInputStream in = new ZInputStream(compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        return total;
    }

}
