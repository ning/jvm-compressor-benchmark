#!/bin/sh
 
echo "About to run test on Canterbury corpus files"

# No huge files, default mem should do
# Since there are 11 input files, 2 modes (comp/uncomp),
# group by... hmmh. 5.5 would be optimal. But I guess 6 has to do?

java -server -cp lib/japex/\* \
 -Xmx512M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/canterbury-compress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/canterbury" \
 com.sun.japex.Japex \
 cfg/tests-canterbury-compress.xml

echo "Done!";
