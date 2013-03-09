#!/bin/sh
 
echo "About to run uncompress test on Silesia corpus files"

# Since there are 12 input files group by 6

java -server -cp lib/japex/\* \
 -Xmx500M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/silesia-uncompress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/silesia" \
 com.sun.japex.Japex \
 cfg/tests-silesia-uncompress.xml

echo "Done!";

