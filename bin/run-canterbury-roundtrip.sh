#!/bin/sh
 
echo "About to run test on Canterbury corpus files"

# Nothing big stored in memory, heap can remain modest 
# Since there are 11 input files, 2 modes (comp/uncomp),
# group by... hmmh. 5.5 would be optimal. But I guess 6 has to do?

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=5 -Djapex.runTime=10 \
 -Djapex.plotGroupSize=6 \
 -Djapex.reportsDirectory=reports/canterbury-roundtrip \
 -Djapex.inputDir="testdata/canterbury" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-canterbury-roundtrip.xml

echo "Done!";
