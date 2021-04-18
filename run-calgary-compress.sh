#!/bin/sh
 
echo "About to run Compress test for All impls on Calgary corpus files"

java -server -Xmx400M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/calgary-compress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir=testdata/calgary \
 -jar target/jvm-compressor-benchmark-*.jar \
 -verbose -merge \
 cfg/tests-calgary-compress.xml

echo "Done!";

