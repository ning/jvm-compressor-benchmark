# Overview

This project is focus on building a comprehensive benchamrk for comparing time and space efficiency of open source compression codecs on JVM platform.
Codecs to include need to be accesible from Java (and thereby from any JVM language) via either pure Java interface or JNI; and need to support either basic block mode (byte array in, byte array out), or streaming code (InputStream in, OutputStream out).

Benchmark suite is based on Japex framework (http://japex.java.net/).

In addition to benchmark itself, we also provide access to set of benchmark results, which can be used for overview of general performance patterns for standard test suites. It is recommended, however, to run tests yourself since they vary depending on platform. In addition, to get more accurate understanding of how results apply to your use case(s), the best thing to do is to collect specific set of test data that reflects your usage, and run tests over this.

For more completed description, checkout out project wiki: https://github.com/ning/jvm-compressor-benchmark/wiki

# Codecs included

Currently following codecs are included in distribution:

* LZF from [https://github.com/ning/compress] (block and streaming modes)
* QuickLZ from [http://www.quicklz.com/] (block mode)
* Gzip: both JDK, and JCraft ones (streaming mode)
* Bzip2 from commons-compression (streaming mode)

Since there are two basic compression modes (block mode, streaming mode), there are either one or two tests per codec.

In addition to codecs included, we are aware of other JVM codecs that we can not yet support (due to API or licensing); as well as codecs for which a JVM-accessible version may be forthcoming.
These included

* FastLZ (http://www.fastlz.org/): no Java version
* LZMA by 7-zip (http://www.7-zip.org/sdk.html): Java version exists, but API not streaming (only supports reading via InputStream AND writing to OutputStream; which works for encoding files but not for many other use cases)
* LZO (from http://www.oberhumer.com/opensource/lzo/): only Java decompressor, no compressor (test suite needs both, to generate compressed data for decompression)
* Snappy (http://code.google.com/p/snappy/): no Java version or JNI wrapper (yet?)

# Getting involved

To access source, just clone project: https://github.com/ning/jvm-compressor-benchmark

To participate in discussions of benchmark suite, results, and other things related to compression performance, please join our discussion group at http://groups.google.com/group/jvm-compressor-benchmark

