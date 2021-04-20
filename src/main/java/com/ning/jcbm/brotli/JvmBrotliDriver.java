package com.ning.jcbm.brotli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import com.nixxcode.jvmbrotli.dec.Decoder;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;
import com.ning.jcbm.DriverBase;

// Brotli with default quality setting (-1)
public class JvmBrotliDriver extends DriverBase
{
    public JvmBrotliDriver()
    {
        super("Brotli/jvmbrotli");
        // As per [https://jvmbrotli.com/examples], must call at least once:
        BrotliLoader.isBrotliAvailable();
    }

    @Override
    protected int maxCompressedLength(int length) {
        // Not sure if there is a good way to do it otherwise, so snatch this
        // from iq80 snappy impl
        return 32 + length + (length / 6);
    }

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer)
            throws IOException
    {
        byte[] encoded = Encoder.compress(uncompressed);
        System.arraycopy(encoded, 0, compressBuffer, 0, encoded.length);
        return encoded.length;
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer)
            throws IOException
    {
        byte[] decoded = Decoder.decompress(compressed);
        System.arraycopy(decoded, 0, uncompressBuffer, 0, decoded.length);
        return decoded.length;
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
            throws IOException
    {
        // Use default quality level (-1 marker), window length
        try (BrotliOutputStream out = new BrotliOutputStream(rawOut)) {
            out.write(uncompressed);
        }
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
            throws IOException
    {
        int total = 0;
        try (BrotliInputStream in = new BrotliInputStream(compIn)) {
            int count;
            while ((count = in.read(inputBuffer)) >= 0) {
                total += count;
            }
        }
        return total;
    }
}
