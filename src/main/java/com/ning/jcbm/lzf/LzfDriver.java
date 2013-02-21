package com.ning.jcbm.lzf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.compress.lzf.*;

import com.ning.jcbm.DriverBase;

public class LzfDriver extends DriverBase
{
    public LzfDriver()
    {
        super("LZF");
    }

    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        // uses new "appendEncoded" in 0.9.7:
        int outPtr = LZFEncoder.appendEncoded(uncompressed, 0, uncompressed.length,
                compressBuffer, 0);
        return outPtr;
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        return LZFDecoder.decode(compressed, 0, compressed.length, uncompressBuffer);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
        throws IOException
    {
        LZFOutputStream out = new LZFOutputStream(rawOut);
        out.write(uncompressed);
        out.close();
    }

    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
        throws IOException
    {
        LZFInputStream in = new LZFInputStream(compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        in.close();
        return total;
    }
}
