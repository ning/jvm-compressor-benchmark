#!/bin/sh
 
echo "About to run test on Calgary corpus files"

# Nothing big stored in memory, heap can remain modest 
# Since there are 18 input files, 2 modes (comp/uncomp),
# group by 6 (9 gets bit too crowded)

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true -Djapex.numberOfThreads=1 \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=7 -Djapex.runTime=30 \
 -Djapex.plotGroupSize=6 \
 -Djapex.reportsDirectory=reports/calgary-roundtrip \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar -verbose \
 cfg/tests-calgary-roundtrip.xml

echo "Done!";
