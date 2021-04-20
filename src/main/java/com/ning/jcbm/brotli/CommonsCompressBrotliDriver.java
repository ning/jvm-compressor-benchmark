package com.ning.jcbm.brotli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;

import com.ning.jcbm.DriverBase;

public class CommonsCompressBrotliDriver extends DriverBase
{
    public CommonsCompressBrotliDriver()
    {
        super("Brotli/ApacheCC");
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
        // Apache Commons-Compress 1.20 only provides read-only version:
        throw new UnsupportedOperationException();
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        int total = 0;
        try (BrotliCompressorInputStream in = new BrotliCompressorInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
