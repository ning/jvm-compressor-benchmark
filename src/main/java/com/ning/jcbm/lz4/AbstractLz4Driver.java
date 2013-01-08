package com.ning.jcbm.lz4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

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
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        final int decompressedLength = uncompressed.length;
        final int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        final byte[] compressed = new byte[4 + maxCompressedLength];
        compressed[0] = (byte) decompressedLength;
        compressed[1] = (byte) (decompressedLength >>> 8);
        compressed[2] = (byte) (decompressedLength >>> 16);
        compressed[3] = (byte) (decompressedLength >>> 24);
        final int compressedLength = compressor.compress(
                uncompressed, 0, decompressedLength,
                compressed, 4, maxCompressedLength);
        return Arrays.copyOf(compressed, 4 + compressedLength);
    }

    @Override
    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        assert compressed.length > 4;
        final int decompressedLength =
                (compressed[0] & 0xFF)
                | ((compressed[1] & 0xFF) << 8)
                | ((compressed[2] & 0xFF) << 16)
                | ((compressed[3] & 0xFF) << 24);
        final byte[] restored = new byte[decompressedLength];
        final int compressedLength = DECOMPRESSOR.decompress(compressed, 4, restored, 0, decompressedLength);
        assert compressedLength == compressed.length;
        return restored;
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
