package com.ning.jcbm.iq80snappy;

import com.ning.jcbm.DriverBase;
import org.iq80.snappy.Snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

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

    protected byte[] compressBlock(byte[] uncompressed)
            throws IOException
    {
        byte[] compressed = new byte[Snappy.maxCompressedLength(uncompressed.length)];
        int compressedSize = Snappy.compress(uncompressed, 0, uncompressed.length, compressed, 0);
        return Arrays.copyOf(compressed, compressedSize);
    }

    protected byte[] uncompressBlock(byte[] compressed)
            throws IOException
    {
        byte[] uncompressed = Snappy.uncompress(compressed, 0, compressed.length);
        return uncompressed;
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
