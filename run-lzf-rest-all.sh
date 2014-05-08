#!/bin/sh
 
echo "About to run Compress/Decompress/Roundtrip test for LZF & GZIP impls on REST corpus files"
#Note: config modified to mimick a specific appplication server config

java -server -cp lib/japex/\* \
 -Xms786m -Xmx1572m -XX:+DisableExplicitGC \
 -XX:PermSize=128m -XX:PermSize=128m -XX:PermSize=128m \
 -Djava.awt.headless=true \
 -Djapex.runsPerDriver=1 \
 -Djapex.warmupTime=7 \
 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/lzfs-compress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/rest" \
 com.sun.japex.Japex \
 cfg/tests-lzf-rest-all.xml

echo "Done!";

