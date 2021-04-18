#!/bin/sh
 
echo "About to run Compress test for LZF impls on Calgary corpus files"

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=7 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/lzfs-compress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar \
 cfg/tests-lzf-compress.xml

echo "Done!";

