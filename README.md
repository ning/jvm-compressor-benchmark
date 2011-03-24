Project for comparing both time and space efficiency of open source compression codes on JVM platform.

Benchmark done using Japex framework (http://japex.java.net/).

Test modes:

* Block/streaming (some codecs only support block mode)

Codecs included:

* LZF from [https://github.com/ning/compress]
* QuickLZ from [http://www.quicklz.com/]
* Gzip: both JDK, and JCraft ones
* Bzip2 from commons-compression

Codecs considered but not included:

* LZO (from http://www.oberhumer.com/opensource/lzo/) -- only Java decompressor, no compressor
* FastLZ no Java version
* Snappy -- no Java version or JNI wrapper (yet?)
