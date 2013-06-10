package com.ning.jcbm.gzip;

import java.io.*;

import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;

import com.ning.jcbm.DriverBase;

public class JCraftGzipDriver extends DriverBase
{
    // Which compression level should we use? Multiple? Start with half-way
    private final static int COMP_LEVEL = 5;

    // do we want gzip wrapping or not?
    private final static boolean NO_WRAP = true;
    
    public JCraftGzipDriver() {
        super("GZIP(jcraft)");
    }

    // No native Block API; but need some impl for test framework

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException {
        return compressBlockUsingStream(uncompressed, compressBuffer);
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException {
        return uncompressBlockUsingStream(new ZInputStream(new ByteArrayInputStream(compressed), NO_WRAP), uncompressBuffer);
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        ZOutputStream out = new ZOutputStream(rawOut, COMP_LEVEL, NO_WRAP);
        out.write(uncompressed);
        out.close();
    }
    
    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        ZInputStream in = new ZInputStream(compIn, NO_WRAP);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        in.close();
        return total;
    }

}
