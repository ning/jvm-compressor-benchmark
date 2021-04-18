#!/bin/sh
 
echo "About to run minimal sanity test on 3 input files, couple of codecs"

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=5 -Djapex.runTime=15 \
 -Djapex.plotGroupSize=5 \
 -Djapex.reportsDirectory=reports/minimal \
 -Djapex.inputDir="testdata/canterbury" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-minimal.xml

echo "Done!";
