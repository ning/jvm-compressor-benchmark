#!/bin/sh
 
echo "About to run uncompress test on Calgary corpus files"

# Since there are 18 input files
# group by 6 (9 gets bit too crowded)

java -server -cp lib/japex/\* \
 -Xmx128M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/calgary \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 com.sun.japex.Japex \
 cfg/tests-calgary-uncompress.xml

echo "Done!";
