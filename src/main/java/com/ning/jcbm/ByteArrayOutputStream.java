package com.ning.jcbm;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Same as {@link java.io.ByteArrayOutputStream} but allows to wrap an existing
 * array.
 */
public class ByteArrayOutputStream extends OutputStream {

    private final byte[] wrapped;
    private int length;

    public ByteArrayOutputStream(byte[] wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Cannot wrap a nul array");
        }
        this.wrapped = wrapped;
        this.length = 0;
    }

    public byte[] array() {
        return wrapped;
    }

    public int length() {
        return length;
    }

    @Override
    public void write(int b) throws IOException {
        wrapped[length++] = (byte) b;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        System.arraycopy(b, off, wrapped, length, len);
        length += len;
    }

}
