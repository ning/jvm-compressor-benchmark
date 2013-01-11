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

    @Override
    protected int maxCompressedLength(int length) {
        return _codec.MaxCompressedSize(length);
    }

    protected int compressBlock(byte[] src, byte[] compressBuffer) throws IOException
    {
        return _codec.Compress(src, 0, src.length, compressBuffer, 0);
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        return _codec.Decompress(compressed, 0, compressed.length, uncompressBuffer, 0);
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
