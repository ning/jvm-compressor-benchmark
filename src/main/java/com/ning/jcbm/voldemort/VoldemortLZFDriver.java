package com.ning.jcbm.voldemort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.jcbm.DriverBase;

public class VoldemortLZFDriver extends DriverBase
{
    public VoldemortLZFDriver()
    {
        super("LZF");
    }

    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        byte[] compressed = VoldemortLZFEncoder.encode(uncompressed);
        System.arraycopy(compressed, 0, compressBuffer, 0, compressed.length);
        return compressed.length;
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        byte[] decompressed = VoldemortLZFDecoder.decode(compressed);
        System.arraycopy(decompressed, 0, uncompressBuffer, 0, decompressed.length);
        return decompressed.length;
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }
}
