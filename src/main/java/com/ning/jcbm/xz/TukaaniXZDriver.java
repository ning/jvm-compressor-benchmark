package com.ning.jcbm.xz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import com.ning.jcbm.DriverBase;

public class TukaaniXZDriver extends DriverBase
{
    public TukaaniXZDriver()
    {
        super("XZ/tukaani");
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
        return compressBlockUsingStream(uncompressed, compressBuffer);
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        try (InputStream in = new ByteArrayInputStream(compressed)) {
            try (XZInputStream xzIn = new XZInputStream(in)) {
                return uncompressBlockUsingStream(xzIn, uncompressBuffer);
            }
        }
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
            throws IOException
    {
        // Use default quality level (-1 marker), window length
        try (XZOutputStream out = new XZOutputStream(rawOut,
                new LZMA2Options())) {
            out.write(uncompressed);
        }
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        int total = 0;
        try (XZInputStream in = new XZInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
