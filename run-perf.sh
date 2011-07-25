#!/bin/sh

java -cp build/classes:lib/lzma-java-exp/\*:lib/japex/\* \
 perf.ManualPerfComparison  testdata/calgary/bib
