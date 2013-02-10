package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * High LZ4 compression, pure Java impl relying on sun.misc.Unsafe.
 */
public class Lz4HcJavaUnsafeDriver extends AbstractLz4Driver {

    public Lz4HcJavaUnsafeDriver() {
        super("LZ4 HC (Java+Unsafe)", LZ4Factory.unsafeInstance().highCompressor(), LZ4Factory.unsafeInstance().decompressor());
    }

}
