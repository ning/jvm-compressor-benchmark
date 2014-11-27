package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * High LZ4 compression, pure Java impl.
 */
public class Lz4HcJavaDriver extends AbstractLz4Driver {

    public Lz4HcJavaDriver() {
        super("LZ4 HC (Java)", LZ4Factory.safeInstance().highCompressor(), LZ4Factory.safeInstance().fastDecompressor());
    }

}
