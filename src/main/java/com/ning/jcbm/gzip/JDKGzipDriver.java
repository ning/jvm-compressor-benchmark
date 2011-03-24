package com.ning.jcbm.gzip;

import java.io.*;
import java.util.zip.*;

import com.ning.jcbm.DriverBase;

public class JDKGzipDriver extends DriverBase
{
    public JDKGzipDriver() {
        super("GZIP(jdk)");
    }

    // No native Block API; but need some impl for test framework
    
    protected byte[] compressBlock(byte[] uncompressed) throws IOException {
        return compressBlockUsingStream(uncompressed);
    }

    protected byte[] uncompressBlock(byte[] compressed) throws IOException {
        return uncompressBlockUsingStream(compressed);
    }

    protected void compressToStream(byte[] uncompressed, OutputStream rawOut) throws IOException
    {
        GZIPOutputStream out = new GZIPOutputStream(rawOut, 4000);
        out.write(uncompressed);
        out.close();
    }
    
    protected int uncompressFromStream(InputStream compIn, byte[] inputBuffer) throws IOException
    {
        GZIPInputStream in = new GZIPInputStream(compIn);

        int total = 0;
        int count;
        
        while ((count = in.read(inputBuffer)) >= 0) {
            total += count;
        }
        return total;
    }
}
