package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * Standard LZ4 compression, pure Java impl relying on sun.misc.Unsafe.
 */
public class Lz4JavaUnsafeDriver extends AbstractLz4Driver {

    public Lz4JavaUnsafeDriver() {
        super("LZ4 (Java+Unsafe)", LZ4Factory.unsafeInstance().fastCompressor(), LZ4Factory.unsafeInstance().decompressor());
    }

}
