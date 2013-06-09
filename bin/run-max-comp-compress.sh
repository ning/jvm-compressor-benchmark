#!/bin/sh
 
echo "About to run compress-only test with 'maximum compression' test data"

# Nothing big stored in memory, heap can remain modest 
# Since there are 10 input files, group by 5
java -server -cp lib/japex/\*  \
 -Djava.library.path=lib/jni \
 -Xmx512M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/maxcomp-compress \
 -Djapex.plotGroupSize=5 \
 -Djapex.inputDir="testdata/maximumcompression" \
 com.sun.japex.Japex \
 cfg/tests-max-comp-compress.xml

echo "Done!";
