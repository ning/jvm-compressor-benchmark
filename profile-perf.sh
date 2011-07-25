#!/bin/sh

java -cp build/classes:lib/lzma-java-exp/\*:lib/japex/\* \
  -Xrunhprof:cpu=samples,depth=10,verbose=n,interval=2 \
 perf.ManualPerfComparison  testdata/calgary/bib
