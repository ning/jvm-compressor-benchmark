package com.ning.jcbm.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.jcbm.DriverBase;

import org.xerial.snappy.*;

public class SnappyDriver extends DriverBase
{
    public SnappyDriver() {
        super("Snappy");
    }

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        try {
            return Snappy.compress(uncompressed);
        } catch (SnappyException e) {
            throw new IOException(e);
        }
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        try {
            return Snappy.uncompress(compressed);
        } catch (SnappyException e) {
            throw new IOException(e);
        }
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        SnappyOutputStream out = new SnappyOutputStream(rawOut);
        out.write(uncompressed);
        out.close();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        SnappyInputStream in = new SnappyInputStream(compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        in.close();
        return total;
    }
}
