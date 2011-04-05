#!/bin/sh
 
echo "About to run test on tests from http://quicklz.com"

# 5 test files (dropped NotTheMusic.mp4 since it doesn't compress
# and we have plenty of such files); group by 5
java -server -cp lib/japex/\* \
 -Xmx128M \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/quicklz-compress \
 -Djapex.plotGroupSize=5 \
 -Djapex.inputDir="testdata/quicklz" \
 com.sun.japex.Japex \
 cfg/tests-quicklz-compress.xml

echo "Done!";
