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

    // No native Block API; but need some impl for test framework
    
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(DEFAULT_ALGORITHM, null);
        LzoInputStream uncompressed = new LzoInputStream(new ByteArrayInputStream(compressed), decompressor);
        return uncompressBlockUsingStream(uncompressed);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(DEFAULT_ALGORITHM, null);
        LzoOutputStream compressedOut = new LzoOutputStream(rawOut, compressor, 256);
        compressedOut.write(uncompressed);
        compressedOut.close();
    }
    
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
