package com.ning.jcbm.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.ning.jcbm.DriverBase;

import org.xerial.snappy.*;

public class SnappyDriver extends DriverBase
{
    public SnappyDriver() {
        super("Snappy");
    }

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        ByteBuffer input = ByteBuffer.allocateDirect(uncompressed.length);
        input.put(uncompressed);
        input.flip();
        int maxLen = Snappy.maxCompressedLength(uncompressed.length);
        ByteBuffer compressed = ByteBuffer.allocateDirect(maxLen);
        int actual = Snappy.compress(input, compressed);
        byte[] result = new byte[actual];
        compressed.get(result, 0, actual);
        return result;
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        ByteBuffer input = ByteBuffer.allocateDirect(compressed.length);
        input.put(compressed);
        input.flip();
        try {
            int uncompLen = Snappy.uncompressedLength(input);
            ByteBuffer output = ByteBuffer.allocateDirect(uncompLen);
            int actual = Snappy.uncompress(input, output);
            byte[] result = new byte[actual];
            output.get(result, 0, actual);
            return result;
        } catch (SnappyException e) {
            throw new IOException(e);
        }
    }

    /* Streaming operation not supported directly yet; add support if
     * and when lib supports it directly.
     */

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException {
        throw new UnsupportedOperationException();
    }

}
