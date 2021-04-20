package com.ning.jcbm.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.jcbm.DriverBase;

import org.xerial.snappy.*;

public class XerialJNISnappyDriver extends DriverBase
{
    public XerialJNISnappyDriver() {
        super("Snappy/xerial");
    }

    @Override
    protected int maxCompressedLength(int length) {
        return Snappy.maxCompressedLength(length);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        return Snappy.compress(uncompressed, 0, uncompressed.length, compressBuffer, 0);
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        return Snappy.uncompress(compressed, 0, compressed.length, uncompressBuffer, 0);
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        SnappyOutputStream out = new SnappyOutputStream(rawOut);
        out.write(uncompressed);
        out.close();
    }
    
    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        int total = 0;
        try (SnappyInputStream in = new SnappyInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
