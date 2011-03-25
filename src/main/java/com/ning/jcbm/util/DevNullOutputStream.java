package com.ning.jcbm.util;


import java.io.*;

public final class DevNullOutputStream extends OutputStream
{
    public int length = 0;
    
    /**
     * Sum is calculated just to prevent JVM optimizing out
     * operations.
     */
    public int sum;
    
    @Override public void write(int arg0) {
        ++length;
        sum += arg0;
    }

    @Override public void write(byte[] bytes)
    {
        write(bytes, 0, bytes.length);
    }

    @Override public void write(byte[] bytes, int offset, int len) {
        length += len;
        while (offset < len) {
            sum += bytes[offset++];
        }
    }

    @Override public void close() { }
    @Override public void flush() { }
    
    public int length() { return length; }
}
