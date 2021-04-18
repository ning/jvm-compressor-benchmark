#!/bin/sh
 
echo "About to run test with 'maximum compression' test data"

# Nothing big stored in memory, heap can remain modest 
# Since there are 10 input files, 2 modes (comp/uncomp), group by 5

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=7  -Djapex.runTime=30 \
 -Djapex.plotGroupSize=5 \
 -Djapex.reportsDirectory=reports/maxcomp-roundtrip \
 -Djapex.inputDir="testdata/maximumcompression" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-max-comp-roundtrip.xml

echo "Done!";
