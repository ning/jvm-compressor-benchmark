# Overview

This project is focus on building a comprehensive benchmark for comparing time and space efficiency of open source compression codecs on JVM platform.
Codecs to include need to be accesible from Java (and thereby from any JVM language) via either pure Java interface or JNI; and need to support either basic block mode (byte array in, byte array out), or streaming code (InputStream in, OutputStream out).

Benchmark suite is based on [Japex framework](http://japex.java.net/).

In addition to benchmark itself, we also provide access to set of benchmark results, which can be used for overview of general performance patterns for standard test suites. It is recommended, however, to run tests yourself since they vary depending on platform. In addition, to get more accurate understanding of how results apply to your use case(s), the best thing to do is to collect specific set of test data that reflects your usage, and run tests over this.

For more complete description, checkout out project [Wiki](https://github.com/ning/jvm-compressor-benchmark/wiki)

# Codecs included

Currently following codecs are included in distribution:

* [LZF](https://github.com/ning/compress) (block and streaming modes)
* [QuickLZ](http://www.quicklz.com/) (block mode)
* Gzip: JDK, [JCraft](http://www.jcraft.com/jzlib/) (streaming mode)
* [Bzip2 from commons-compression](http://commons.apache.org/compress/) (streaming mode)
* [Snappy](http://code.google.com/p/snappy-java/) (Java JNI wrapper over [native Snappy](http://code.google.com/p/snappy/))
* LZMA:
   * [LZMA by 7zip](http://www.7-zip.org/sdk.html) (block mode)
   * [LZMA-java](https://github.com/jponge/lzma-java) (streaming)
       * note: new (2011/7) addition,  not yet included in results
* [LZO-java](https://github.com/Karmasphere/lzo-java) (streaming, may add block)
 * note: new (2011/7) addition,  not yet included in results
* [LZ4](https://github.com/jpountz/lz4-java) (block mode, bindings and ports of [native LZ4](http://code.google.com/p/lz4/))

(for more details on what is NOT included, check out Wiki page at [https://github.com/ning/jvm-compressor-benchmark/wiki](Wiki))

# Test data used

We have tried to make use of existing de-facto standard test suites, including:

* [Calgary corpus](http://corpus.canterbury.ac.nz/descriptions/#calgary): 18 test files from
* [Canterbury corpus](http://corpus.canterbury.ac.nz/descriptions/#cantrbry): 11 test files
* [Maximum Compression](http://www.maximumcompression.com): 10 test files
* [QuickLZ](http://www.quicklz.com/bench.html): 5 test files
* [Silesia](http://sun.aei.polsl.pl/~sdeor/index.php?page=silesia): 12 test files

# Getting involved

To access source, just clone [project](https://github.com/ning/jvm-compressor-benchmark)

To participate in discussions of benchmark suite, results, and other things related to compression performance, please join our [discussion group](http://groups.google.com/group/jvm-compressor-benchmark)

# License

Benchmark code is licensed under Apache License 2.0.

Note that as usual, license only covers (re)distribution of code, and does not apply to your own use of code (i.e. running tests locally), which you can do regardless of licensing.
