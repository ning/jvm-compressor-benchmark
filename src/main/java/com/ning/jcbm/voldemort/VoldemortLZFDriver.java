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

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        return VoldemortLZFEncoder.encode(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        return VoldemortLZFDecoder.decode(compressed);
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
