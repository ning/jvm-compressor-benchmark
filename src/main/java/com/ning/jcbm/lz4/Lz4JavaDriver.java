package com.ning.jcbm.lz4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.ning.jcbm.DriverBase;

import biz.k11i.compress.lz4.LZ4Codec;

/**
 * Codec for pure-Java lz4 codec from [https://github.com/komiya-atsushi/lz4-java]
 */
public class Lz4JavaDriver extends DriverBase
{
    public Lz4JavaDriver() { super("LZ4/java"); }
    
    protected byte[] compressBlock(byte[] src) throws IOException
    {
        // how to estimate size? Assume at most 1/16 overhead for now
        int srcLen = src.length;
        byte[] dst = new byte[srcLen + 16 + (srcLen >> 4)];
        int len = LZ4Codec.createCompressor().compress(src, dst);
        return Arrays.copyOf(dst, len);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        // how much does it expand? Assume max chunk of 64k?
        byte[] dst = new byte[0x10000 + 1024];
        int len = LZ4Codec.createDecompressor().decompress(compressed, dst);
        return Arrays.copyOf(dst, len);
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
