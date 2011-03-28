# Overview

This project is focus on building a comprehensive benchamrk for comparing time and space efficiency of open source compression codecs on JVM platform.
Codecs to include need to be accesible from Java (and thereby from any JVM language) via either pure Java interface or JNI; and need to support either basic block mode (byte array in, byte array out), or streaming code (InputStream in, OutputStream out).

Benchmark suite is based on Japex framework [http://japex.java.net/](Japex).

In addition to benchmark itself, we also provide access to set of benchmark results, which can be used for overview of general performance patterns for standard test suites. It is recommended, however, to run tests yourself since they vary depending on platform. In addition, to get more accurate understanding of how results apply to your use case(s), the best thing to do is to collect specific set of test data that reflects your usage, and run tests over this.

For more complete description, checkout out project Wiki page: [https://github.com/ning/jvm-compressor-benchmark/wiki](Wiki)

# Codecs included

Currently following codecs are included in distribution:

* LZF from [https://github.com/ning/compress] (block and streaming modes)
* QuickLZ from [http://www.quicklz.com/] (block mode)
* Gzip: both JDK, and JCraft ones (streaming mode)
* Bzip2 from commons-compression (streaming mode)

(for more details on what is NOT included, check out Wiki page at [https://github.com/ning/jvm-compressor-benchmark/wiki](Wiki))

# Test data used

We have tried to make use of existing de-facto standard test suites, including:

* [http://corpus.canterbury.ac.nz/descriptions/#calgary](Calgary corpus): 18 test files from
* [http://corpus.canterbury.ac.nz/descriptions/#cantrbry](Canterbury corpus): 11 test files
* [http://www.maximumcompression.com](Maximum Compression): 10 test files
* [http://www.quicklz.com/bench.html](QuickLZ): 5 test files

# Getting involved

To access source, just clone project from [https://github.com/ning/jvm-compressor-benchmark](jvm-compressor-benchmark@GitHub)

To participate in discussions of benchmark suite, results, and other things related to compression performance, please join our discussion group at http://groups.google.com/group/jvm-compressor-benchmark

