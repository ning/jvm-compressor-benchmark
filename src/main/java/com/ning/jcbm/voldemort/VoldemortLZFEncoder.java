// Copied from Voldemort project; uses Apache License 2.0
package com.ning.jcbm.voldemort;

/**
 * Encoder that handles splitting of input into chunks to encode, calls
 * {@link VoldemortChunkEncoder} to compress individual chunks and combines resulting
 * chunks into contiguous output byte array.
 *<p>
 * Code adapted from H2 project (http://www.h2database.com) Java LZF
 * implementation by Thomas (which itself was inspired by original C code by
 * Marc A Lehmann)
 * 
 */
public class VoldemortLZFEncoder {

    // Static methods only, no point in instantiating
    private VoldemortLZFEncoder() {}

    /**
     * Method for compressing given input data using LZF encoding and block
     * structure (compatible with lzf command line utility). Result consists of
     * a sequence of chunks.
     */
    public static byte[] encode(byte[] data) {
        int left = data.length;
        VoldemortChunkEncoder enc = new VoldemortChunkEncoder(left);
        int chunkLen = Math.min(VoldemortLZFChunk.MAX_CHUNK_LEN, left);
        VoldemortLZFChunk first = enc.encodeChunk(data, 0, chunkLen);
        left -= chunkLen;
        // shortcut: if it all fit in, no need to coalesce:
        if(left < 1) {
            return first.getData();
        }
        // otherwise need to get other chunks:
        int resultBytes = first.length();
        int inputOffset = chunkLen;
        VoldemortLZFChunk last = first;

        do {
            chunkLen = Math.min(left, VoldemortLZFChunk.MAX_CHUNK_LEN);
            VoldemortLZFChunk chunk = enc.encodeChunk(data, inputOffset, chunkLen);
            inputOffset += chunkLen;
            left -= chunkLen;
            resultBytes += chunk.length();
            last.setNext(chunk);
            last = chunk;
        } while(left > 0);
        // and then coalesce returns into single contiguous byte array
        byte[] result = new byte[resultBytes];
        int ptr = 0;
        for(; first != null; first = first.next()) {
            ptr = first.copyTo(result, ptr);
        }
        return result;
    }
}
