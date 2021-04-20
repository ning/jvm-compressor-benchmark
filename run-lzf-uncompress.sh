#!/bin/sh
 
echo "About to run uncompress test for LZF impls on Calgary corpus files"

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=5  -Djapex.runTime=10 \
 -Djapex.plotGroupSize=6 \
 -Djapex.reportsDirectory=reports/lzfs-uncompress \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-lzf-uncompress.xml

echo "Done!";

