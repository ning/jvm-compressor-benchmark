#!/bin/sh
 
echo "About to run compress test on Calgary corpus files"

# Since there are 18 input files
# group by 6 (9 gets bit too crowded)

java -server -cp lib/japex/\* \
 -Xmx400M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/lzfs \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 com.sun.japex.Japex \
 tests-lzf.xml

echo "Done!";

