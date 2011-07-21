#!/bin/sh
 
echo "About to run test on Calgary corpus files"

# Nothing big stored in memory, heap can remain modest 
# Since there are 18 input files, 2 modes (comp/uncomp),
# group by 6 (9 gets bit too crowded)

java -server -cp lib/japex/\* \
 -Xmx400M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/calgary \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 com.sun.japex.Japex \
 cfg/tests-calgary.xml

echo "Done!";
