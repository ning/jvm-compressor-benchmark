package com.ning.jcbm.lz4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Decompressor;
import net.jpountz.lz4.LZ4Factory;

import com.ning.jcbm.DriverBase;

/**
 * LZ4 codecs from https://github.com/jpountz/lz4-java.
 */
public abstract class AbstractLz4Driver extends DriverBase {

    protected static LZ4Factory LZ4_FACTORY = LZ4Factory.fastestInstance();
    private static final LZ4Decompressor DECOMPRESSOR = LZ4_FACTORY.decompressor();

    private final LZ4Compressor compressor;

    protected AbstractLz4Driver(String name, LZ4Compressor compressor) {
        super(name);
        this.compressor = compressor;
    }

    @Override
    protected int maxCompressedLength(int length) {
        return 4 + compressor.maxCompressedLength(length);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException {
        final int decompressedLength = uncompressed.length;
        compressBuffer[0] = (byte) decompressedLength;
        compressBuffer[1] = (byte) (decompressedLength >>> 8);
        compressBuffer[2] = (byte) (decompressedLength >>> 16);
        compressBuffer[3] = (byte) (decompressedLength >>> 24);
        return 4 + compressor.compress(
                uncompressed, 0, decompressedLength,
                compressBuffer, 4, compressBuffer.length - 4);
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException {
        assert compressed.length > 4;
        final int decompressedLength =
                (compressed[0] & 0xFF)
                | ((compressed[1] & 0xFF) << 8)
                | ((compressed[2] & 0xFF) << 16)
                | ((compressed[3] & 0xFF) << 24);
        final int compressedLength = DECOMPRESSOR.decompress(compressed, 4, uncompressBuffer, 0, decompressedLength);
        assert compressedLength == compressed.length;
        return decompressedLength;
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream out) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int uncompressFromStream(InputStream in, byte[] buffer) throws IOException {
        throw new UnsupportedOperationException();
    }

}
