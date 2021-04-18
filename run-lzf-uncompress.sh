#!/bin/sh
 
echo "About to run uncompress test for LZF impls on Calgary corpus files"

java -server -Xmx400M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/lzfs-uncompress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar \
 -verbose \
 cfg/tests-lzf-uncompress.xml

echo "Done!";

