package com.ning.jcbm.lzf;

import java.io.IOException;

import com.ning.compress.lzf.*;

import com.ning.jcbm.DriverBase;

public class LzfDriver extends DriverBase
{
    public LzfDriver()
    {
        super("LZF");
    }

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        return LZFEncoder.encode(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        return LZFDecoder.decode(compressed);
    }
}
