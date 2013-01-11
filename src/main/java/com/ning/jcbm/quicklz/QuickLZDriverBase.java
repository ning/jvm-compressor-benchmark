package com.ning.jcbm.quicklz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.jcbm.DriverBase;

public class QuickLZDriverBase extends DriverBase
{
    protected final int _compressionLevel;
    
    protected QuickLZDriverBase(int level)
    {
        super("QuickLZ/"+level);
        _compressionLevel = level;
    }

    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        byte[] compressed = QuickLZ.compress(uncompressed, _compressionLevel);
        System.arraycopy(compressed, 0, compressBuffer, 0, compressed.length);
        return compressed.length;
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        byte[] decompressed = QuickLZ.decompress(compressed);
        System.arraycopy(decompressed, 0, uncompressBuffer, 0, decompressed.length);
        return decompressed.length;
    }

    /* Streaming operation not supported directly; let's not try faking it
     * since that would not perform well
     */

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException {
        throw new UnsupportedOperationException();
    }
}
