package com.ning.jcbm.quicklz;

import java.io.IOException;

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
}
