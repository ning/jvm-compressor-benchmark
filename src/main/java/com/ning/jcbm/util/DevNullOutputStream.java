package com.ning.jcbm.util;


import java.io.*;

public final class DevNullOutputStream extends OutputStream
{
    public int length = 0;
    
    @Override public void write(int arg0) {
        ++length;
    }

    @Override public void write(byte[] bytes) {
        length += bytes.length;
    }

    @Override public void write(byte[] bytes, int offset, int len) {
        length += len;
    }

    @Override public void close() { }
    @Override public void flush() { }
    
    public int length() { return length; }
}
