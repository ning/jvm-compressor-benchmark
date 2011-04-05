#!/bin/sh
 
echo "About to run test on Canterbury corpus files"

# Nothing big stored in memory, heap can remain modest 
# Since there are 11 input files, 2 modes (comp/uncomp),
# group by... hmmh. 5.5 would be optimal. But I guess 6 has to do?

java -server -cp lib/japex/\* \
 -Xmx128M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/canterbury-uncompress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/canterbury" \
 com.sun.japex.Japex \
 cfg/tests-canterbury-uncompress.xml

echo "Done!";
