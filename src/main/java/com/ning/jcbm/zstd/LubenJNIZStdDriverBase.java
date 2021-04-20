package com.ning.jcbm.zstd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;
import com.ning.jcbm.DriverBase;

// Since Apache Commons-Compress just wraps around this JNI-based
// codec, let's cut the middle-man...
abstract class LubenJNIZStdDriverBase extends DriverBase
{
    protected final int compressionLevel;

    protected LubenJNIZStdDriverBase(int compLevel)
    {
        super("ZStandard-L"+compLevel+"/Luben(JNI)");
        compressionLevel = compLevel;
    }

    @Override
    protected int maxCompressedLength(int length) {
        // Not sure if there is a good way to do it otherwise let's just
        // take a guess...
        return 32 + length + (length / 6);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer)
            throws IOException
    {
        long length = Zstd.compress(compressBuffer, uncompressed,
                compressionLevel);
        return (int) length;
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        long length = Zstd.decompress(uncompressBuffer, compressed);
        return (int) length;
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
            throws IOException
    {
        ZstdOutputStream out = new ZstdOutputStream(rawOut, compressionLevel);
        out.write(uncompressed);
        out.close();
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        int total = 0;
        try (ZstdInputStream in = new ZstdInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
