#!/bin/sh
 
echo "About to run test with 'maximum compression' test data"

# Nothing big stored in memory, heap can remain modest 
# Since there are 10 input files, 2 modes (comp/uncomp), group by 5

java -server -cp lib/japex/\*   \
 -Xmx256M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/maxcomp-roundtrip \
 -Djapex.plotGroupSize=5 \
 -Djapex.inputDir="testdata/maximumcompression" \
 com.sun.japex.Japex \
 cfg/tests-max-comp-roundtrip.xml

echo "Done!";
