package com.ning.jcbm.iq80snappy;

import com.ning.jcbm.DriverBase;
import org.iq80.snappy.Snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Driver for pure-Java Snappy codec from
 * [https://github.com/dain/snappy].
 */
public class Iq80SnappyDriver extends DriverBase
{
    public Iq80SnappyDriver()
    {
        super("iq80.Snappy");
    }

    @Override
    protected int maxCompressedLength(int length) {
        return Snappy.maxCompressedLength(length);
    }

    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer)
            throws IOException
    {
        return Snappy.compress(uncompressed, 0, uncompressed.length, compressBuffer, 0);
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        return Snappy.uncompress(compressed, 0, compressed.length, uncompressBuffer, 0);
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
