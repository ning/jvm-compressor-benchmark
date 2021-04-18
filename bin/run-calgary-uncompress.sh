#!/bin/sh
 
echo "About to run uncompress test on Calgary corpus files"

# Since there are 18 input files
# group by 6 (9 gets bit too crowded)

java -server -Xmx512M \
 -Djava.awt.headless=true -Djapex.contextClassLoader=true \
 -Djapex.runsPerDriver=1 -Djapex.warmupTime=7 -Djapex.runTime=30 \
 -Djapex.numberOfThreads=1 \
 -Djapex.reportsDirectory=reports/calgary-uncompress \
 -Djapex.plotGroupSize=6 \
 -Djapex.inputDir="testdata/calgary" \
 -jar target/jvm-compressor-benchmark-*.jar \
 cfg/tests-calgary-uncompress.xml

echo "Done!";
