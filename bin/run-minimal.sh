#!/bin/sh
 
echo "About to run minimal sanity test on 2 input files, couple of codecs"

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
