package com.ning.jcbm.lzf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ning.compress.lzf.*;
import com.ning.compress.lzf.util.ChunkDecoderFactory;
import com.ning.compress.lzf.util.ChunkEncoderFactory;

import com.ning.jcbm.DriverBase;

public class LzfSafeDriver extends DriverBase
{
    public LzfSafeDriver() {
        super("LZF-safe");
    }
    
    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        int outPtr = LZFEncoder.safeAppendEncoded(uncompressed, 0, uncompressed.length,
                compressBuffer, 0);
        return outPtr;
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        return LZFDecoder.safeDecode(compressed, 0, compressed.length, uncompressBuffer);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut)
        throws IOException
    {
        LZFOutputStream out = new LZFOutputStream(ChunkEncoderFactory.safeInstance(1 << 16), rawOut);
        out.write(uncompressed);
        out.close();
    }

    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer)
        throws IOException
    {
        LZFInputStream in = new LZFInputStream(ChunkDecoderFactory.safeInstance(), compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        in.close();
        return total;
    }
}
