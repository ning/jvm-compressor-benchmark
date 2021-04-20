package com.ning.jcbm.snappy;

import com.ning.jcbm.DriverBase;
import org.iq80.snappy.Snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Driver for pure-Java Snappy codec from
 * [https://github.com/dain/snappy].
 */
public class Iq80JavaSnappyDriver extends DriverBase
{
    public Iq80JavaSnappyDriver()
    {
        super("Snappy/iq80");
    }

    @Override
    protected int maxCompressedLength(int length) {
        return Snappy.maxCompressedLength(length);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer)
            throws IOException
    {
        return Snappy.compress(uncompressed, 0, uncompressed.length, compressBuffer, 0);
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        return Snappy.uncompress(compressed, 0, compressed.length, uncompressBuffer, 0);
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }
}
