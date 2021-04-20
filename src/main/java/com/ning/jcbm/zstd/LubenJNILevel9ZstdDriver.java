package com.ning.jcbm.zstd;

// Version for Level 9 (faster, more modest compression) compressor
public class LubenJNILevel9ZstdDriver
    extends LubenJNIZStdDriverBase
{
    public LubenJNILevel9ZstdDriver() {
        super(9);
    }
}
