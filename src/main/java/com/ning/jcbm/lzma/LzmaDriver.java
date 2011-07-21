package com.ning.jcbm.lzma;

import java.io.*;

import com.ning.jcbm.DriverBase;

/**
 * Driver that uses original conversion done by LZMA author.
 * Codec is not supported any more (AFAIK).
 */
public class LzmaDriver extends DriverBase
{
    static final int DEFAULT_ALGORITHM = 2;
    
    // what would be useful defaults? Should probably depend on input size?
    static final int MAX_DICTIONARY_SIZE = (1 << 21); // default; 2 megs

    static final int DEFAULT_MATCH_FINDER = 1;
    static final int DEFAULT_FAST_BYTES = 128;
    
    static final int Lc = 3;
    static final int Lp = 0;
    static final int Pb = 2;
    
    public LzmaDriver() {
        super("LZMA");
    }

    @Override
    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        ByteArrayInputStream inStream = new ByteArrayInputStream(uncompressed);
        // as a wild guess, use 50% compression
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(10 + (uncompressed.length >> 1));

        boolean eos = true; // what does this mean? that size is not known?
        SevenZip.Compression.LZMA.Encoder encoder = new SevenZip.Compression.LZMA.Encoder();
        if (!encoder.SetAlgorithm(DEFAULT_ALGORITHM)) throw new IllegalArgumentException();
        
        // Let's actually try to determine somewhat optimal size; starting with 4k
        int dictSize = (1 << 12);
        int uncompLen = uncompressed.length;
        while (dictSize < uncompLen) {
            dictSize += dictSize;
            if (dictSize >= MAX_DICTIONARY_SIZE) {
                break;
            }
        }
        
        if (!encoder.SetDictionarySize(dictSize)) throw new IllegalArgumentException();
        if (!encoder.SetNumFastBytes(DEFAULT_FAST_BYTES)) throw new IllegalArgumentException();
        if (!encoder.SetMatchFinder(DEFAULT_MATCH_FINDER)) throw new IllegalArgumentException();
        if (!encoder.SetLcLpPb(Lc, Lp, Pb)) throw new IllegalArgumentException();
        encoder.SetEndMarkerMode(eos);
        encoder.WriteCoderProperties(outStream);
        for (int i = 0; i < 8; i++) { // just write -1
            outStream.write(0xFF);
        }
        encoder.Code(inStream, outStream, -1, -1, null);
        byte[] result = outStream.toByteArray();
        
        return result;
    }

    @Override
    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        ByteArrayInputStream inStream = new ByteArrayInputStream(compressed);
        int propertiesSize = 5;
        byte[] properties = new byte[propertiesSize];
        if (inStream.read(properties, 0, propertiesSize) != propertiesSize) {
            throw new IOException("input .lzma content is too short");
        }
        SevenZip.Compression.LZMA.Decoder decoder = new SevenZip.Compression.LZMA.Decoder();
        if (!decoder.SetDecoderProperties(properties)) {
            throw new IOException("Incorrect stream properties");
        }
        long outSize = 0;
        for (int i = 0; i < 8; i++) {
            int v = inStream.read();
            if (v < 0) {
                throw new IOException("Can't read stream size");
            }
            outSize = (outSize << 8) + (v & 0xFF);
        }
        ByteArrayOutputStream outStream;
        if (outSize <= 0L) { // if not known, assume 50% compression (i.e. 2x expansion)
            outStream = new ByteArrayOutputStream(compressed.length << 1);
        } else {
            outStream = new ByteArrayOutputStream((int) outSize);
        }
        
        if (!decoder.Code(inStream, outStream, outSize)) {
            throw new IOException("Error in data stream");
        }
        return outStream.toByteArray();
    }

        
    /* Streaming not natively supported (or rather, not in the way we could use it);
     * could fake by using block mode, but let's not yet bother.
     */
    
    @Override
    protected void compressToStream(byte[] uncompressed, OutputStream out) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int uncompressFromStream(InputStream in, byte[] buffer) throws IOException {
        throw new UnsupportedOperationException();
    }
}
