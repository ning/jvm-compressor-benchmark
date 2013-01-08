package com.ning.jcbm.lz4;

/**
 * Standard LZ4 compression.
 */
public class Lz4Driver extends AbstractLz4Driver {

    public Lz4Driver() {
        super("LZ4", LZ4_FACTORY.fastCompressor());
    }

}
