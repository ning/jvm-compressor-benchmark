package com.ning.jcbm.lzma;

import java.io.*;

import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;
import lzma.sdk.lzma.Decoder;

import com.ning.jcbm.DriverBase;

/**
 * Driver for more up-to-date LZMA codec from
 * [https://github.com/jponge/lzma-java].
 * Project is active, code maintained.
 */
public class LzmaJavaDriver extends DriverBase
{
    public LzmaJavaDriver() {
        super("LZMA-java");
    }

    // No native Block API; but need some impl for test framework
    
    public byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    public byte[] uncompressBlock(byte[] compressed) throws IOException {
        return uncompressBlockUsingStream(
                new LzmaInputStream(new ByteArrayInputStream(compressed), getDecoder()));
    }

    public void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        LzmaOutputStream compressedOut = new LzmaOutputStream.Builder(
                // do we really need compressed stream here? probably not, it's ByteArrayOutputStream
                //new BufferedOutputStream(rawOut))
                rawOut)
        // how about other settings? are defaults ok? If not, these are suggestions from project page:
        /*
                .useMaximalDictionarySize()
                .useEndMarkerMode(true)
                .useBT4MatchFinder()
                */
        // huge dictionary uses memory, slow; minimal is useless. Medium is (1 << 15), Max (1 << 28),
        // default (1 << 23)
            .useMediumDictionarySize()
            .useMediumFastBytes()
                .build();
        compressedOut.write(uncompressed);
        compressedOut.close();
    }
    
    public int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        // as above, we get a ByteArrayInputStream so no buffering needed:
        // (but would reuse of Decoder help?)
        LzmaInputStream compressedIn = new LzmaInputStream(compIn, getDecoder());

        int total = 0;
        int count;
        
        while ((count = compressedIn.read(inputBuffer)) >= 0) {
            total += count;
        }
        compressedIn.close();
        return total;
    }
 
    protected Decoder getDecoder() {
        return new Decoder();
    }
}
