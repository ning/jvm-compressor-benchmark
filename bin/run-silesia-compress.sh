#!/bin/sh
 
echo "About to run compress test on Silesia corpus files"

# Since there are 12 input files group by 6
# Bigger heap needed for some codecs

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=7  -Djapex.runTime=30 \
 -Djapex.plotGroupSize=6 \
 -Djapex.reportsDirectory=reports/silesia-compress \
 -Djapex.inputDir="testdata/silesia" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-silesia-compress.xml

echo "Done!";

