package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * High LZ4 compression, JNI bindings.
 */
public class Lz4HcJNIDriver extends AbstractLz4Driver {

    public Lz4HcJNIDriver() {
        super("LZ4 HC (JNI)", LZ4Factory.nativeInstance().highCompressor(), LZ4Factory.nativeInstance().decompressor());
    }

}
