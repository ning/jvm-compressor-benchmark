package com.ning.jcbm.lz4;

import net.jpountz.lz4.LZ4Factory;

/**
 * Standard LZ4 compression, pure Java impl.
 */
public class Lz4JavaDriver extends AbstractLz4Driver {

    public Lz4JavaDriver() {
        super("LZ4 (Java)", LZ4Factory.safeInstance().fastCompressor(), LZ4Factory.safeInstance().fastDecompressor());
    }

}
