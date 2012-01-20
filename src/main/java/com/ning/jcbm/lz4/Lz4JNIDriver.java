package com.ning.jcbm.lz4;

import java.io.*;

import com.ning.jcbm.DriverBase;

import com.github.decster.jnicompressions.Lz4Compression;

/**
 * Codec for Java JNI wrapper for LZ4 codec from
 * [https://github.com/decster/jnicompressions]
 */
public class Lz4JNIDriver extends DriverBase
{
    private final Lz4Compression _codec;
    
    public Lz4JNIDriver() {
        super("LZ4/JNI");
        _codec = new Lz4Compression();
    }
    
    protected byte[] compressBlock(byte[] src) throws IOException
    {
        return _codec.CompressSimple(src);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        return _codec.DecompressSimple(compressed);
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
