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

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        return QuickLZ.compress(uncompressed, _compressionLevel);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        return QuickLZ.decompress(compressed);
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
