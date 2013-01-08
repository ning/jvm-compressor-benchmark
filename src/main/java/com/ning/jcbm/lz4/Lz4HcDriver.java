package com.ning.jcbm.lz4;

/**
 * High LZ4 compression.
 */
public class Lz4HcDriver extends AbstractLz4Driver {

    public Lz4HcDriver() {
        super("LZ4", LZ4_FACTORY.highCompressor());
    }

}
