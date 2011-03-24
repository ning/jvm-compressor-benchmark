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

    protected byte[] compressBlock(byte[] uncompressed) throws IOException
    {
        return LZFEncoder.encode(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException
    {
        return LZFDecoder.decode(compressed);
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
        return total;
    }
}
