package com.ning.jcbm.gzip;

import java.io.*;
import java.util.zip.*;

import com.ning.jcbm.DriverBase;

public class JDKGzipDriver extends DriverBase
{
    /**
     * By default let's use DEFAULT_COMPRESSION, as that's what one gets
     * out of the box.
     */
    protected final static int COMPRESSION_LEVEL = Deflater.DEFAULT_COMPRESSION;
    
    protected final static boolean NO_WRAP = true;
    
    protected final Deflater _deflater;
    protected final Inflater _inflater;
    
    public JDKGzipDriver() {
        super("GZIP(jdk)");
        _deflater = new Deflater(COMPRESSION_LEVEL, NO_WRAP);
        _inflater = new Inflater(NO_WRAP);
    }
    
    // No native Block API; but need some impl for test framework
    
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        _inflater.reset();
        return uncompressBlockUsingStream(new InflaterInputStream(new ByteArrayInputStream(compressed), _inflater));
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        _deflater.reset();
        DeflaterOutputStream out = new DeflaterOutputStream(rawOut, _deflater, 4000);
        out.write(uncompressed);
        out.close();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        _inflater.reset();
        InflaterInputStream in = new InflaterInputStream(compIn, _inflater);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        return total;
    }
}
