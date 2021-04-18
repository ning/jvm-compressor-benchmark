package com.ning.jcbm.lzo;

import java.io.*;

import org.anarres.lzo.*;

import com.ning.jcbm.DriverBase;

/**
 * Driver for pure Java LZO codec from
 * [https://github.com/Karmasphere/lzo-java].
 */
public class LzoJavaDriver extends DriverBase
{
    private final static LzoAlgorithm DEFAULT_ALGORITHM = LzoAlgorithm.LZO1X;
    
    public LzoJavaDriver() {
        super("LZO-java");
    }

    @Override
    protected int maxCompressedLength(int length) {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(DEFAULT_ALGORITHM, null);
        return 8 + length + compressor.getCompressionOverhead(length);
    }

    // No native Block API; but need some impl for test framework

    @Override
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(DEFAULT_ALGORITHM, null);
        // Looks like we need to allocate a big buffer, and see how much data
        // we get... definitely C-style interface
        //

        int origLength = uncompressed.length;
        lzo_uintp lengthPointer = new lzo_uintp(origLength); // ugh....
        int resultCode = compressor.compress(uncompressed, 0, origLength,
                compressBuffer, 8, lengthPointer);
        if (resultCode != LzoTransformer.LZO_E_OK) {
            throw new IOException("Error code "+resultCode+" from compressor");
        }
        // Hmmh. Is 'value' length, or offset? Looks like length...
        int compLength = lengthPointer.value;

        writeInt32(compressBuffer, 0, origLength);
        writeInt32(compressBuffer, 4, compLength);
        return compLength+8;
    }

    private final void writeInt32(byte[] buffer, int offset, int value)
    {
        buffer[offset++] = (byte) (value >> 24);
        buffer[offset++] = (byte) (value >> 16);
        buffer[offset++] = (byte) (value >> 8);
        buffer[offset] = (byte) value;
    }

    @Override
    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        /* Alas, we can't really support true block decompression since we
         * would need to know the uncompressed length a priori as codec
         * does not seem able to auto-detect it.
         */
        LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(DEFAULT_ALGORITHM, null);
        LzoInputStream uncompressed = new LzoInputStream(new ByteArrayInputStream(compressed), decompressor);
        return uncompressBlockUsingStream(uncompressed, uncompressBuffer);
    }

    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(DEFAULT_ALGORITHM, null);
        /* what would be good buffer size? 256 from tests is tiny; plus, does this limit
         * chunk/block length? Let's try 16k blocks
         */
        LzoOutputStream compressedOut = new LzoOutputStream(rawOut, compressor, 16000);
        compressedOut.write(uncompressed);
        compressedOut.close();
    }

    @Override
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(DEFAULT_ALGORITHM, null);
        LzoInputStream uncompressed = new LzoInputStream(compIn, decompressor);

        int total = 0;
        int count;
        
        while ((count = uncompressed.read(inputBuffer)) >= 0) {
            total += count;
        }
        uncompressed.close();
        return total;
    }
}
