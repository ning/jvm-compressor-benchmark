#!/bin/sh
 
echo "About to run Compress/Decompress/Roundtrip test for LZF & GZIP impls on REST corpus files"
#Note: config modified to mimick a specific appplication server config

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=5  -Djapex.runTime=10 \
 -Djapex.plotGroupSize=6 \
 -Djapex.resultUnitX=CompressRatio \
 -Djapex.resultUnitY=MBps \
 -Djapex.reportsDirectory=reports/lzfs-compress \
 -Djapex.inputDir="testdata/rest" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-lzf-rest-roundtrip.xml

echo "Done!";

