#!/bin/sh
 
echo "About to run Compress test for LZF impls on Calgary corpus files"

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=5 -Djapex.runTime=10 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/snappy-compress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-snappy-compress.xml

echo "Done!";

