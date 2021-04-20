package com.ning.jcbm.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorOutputStream;

import com.ning.jcbm.DriverBase;

public class CommonsCompressSnappyDriver extends DriverBase
{
    public CommonsCompressSnappyDriver()
    {
        super("Snappy/ApacheCC");
    }

    @Override
    protected int maxCompressedLength(int length) {
        // Not sure if there is a good way to do it otherwise, so snatch this
        // from iq80 snappy impl
        return 32 + length + (length / 6);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
            throws IOException
    {
        // Not cool that we have to provide the original size (it's a _stream_)
        // but... *shrug*
        SnappyCompressorOutputStream out = new SnappyCompressorOutputStream(rawOut,
                uncompressed.length);
        out.write(uncompressed);
        out.close();
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        int total = 0;
        try (SnappyCompressorInputStream in = new SnappyCompressorInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
