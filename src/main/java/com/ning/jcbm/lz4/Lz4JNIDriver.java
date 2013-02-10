package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * Standard LZ4 compression, JNI bindings.
 */
public class Lz4JNIDriver extends AbstractLz4Driver {

    public Lz4JNIDriver() {
        super("LZ4 (JNI)", LZ4Factory.nativeInstance().fastCompressor(), LZ4Factory.nativeInstance().decompressor());
    }

}
