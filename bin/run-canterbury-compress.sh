#!/bin/sh
 
echo "About to run test on Canterbury corpus files"

# LZO has some issues with memory usage, apparently need to increase?
# Since there are 11 input files, 2 modes (comp/uncomp),
# group by... hmmh. 5.5 would be optimal. But I guess 6 has to do?

java -server -cp lib/japex/\* \
 -Xmx500M \
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
