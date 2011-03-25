#!/bin/sh
 
echo "About to run minimal sanity test on 2 input files, couple of codecs"

# Nothing big stored in memory, heap can remain modest 
# Since there are 11 input files, 2 modes (comp/uncomp),
# group by... hmmh. 5.5 would be optimal. But I guess 11 has to do?

java -server -cp lib/japex/\* \
 -Xmx128M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=5 \
 -Djapex.runTime=15 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/minimal \
 -Djapex.plotGroupSize=5 \
 -Djapex.inputDir="testdata/canterbury" \
 com.sun.japex.Japex \
 cfg/tests-minimal.xml

echo "Done!";
