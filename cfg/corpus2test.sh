#!/bin/bash
#Create tests for a folder of files, given as first arg
FOLDER=$1 #Corpus name
FILES=`cd ../testdata/$1 && ls | sort`
echo '<?xml version="1.0" encoding="UTF-8"?>

<testSuite name="Test suite for JVM compressors"
  xmlns="http://www.sun.com/japex/testSuite"
  xmlns:xi="http://www.w3.org/2001/XInclude"
>
    <description><div xmlns=""><p>
Micro-benchmark for testing performance of compressors on JVM platform
</p></div></description>

    <!-- Drivers -->
    <xi:include href="drivers-lzf-gzip.xml" />
    ' > tests-lzf-$FOLDER-compress.xml

cp tests-lzf-$FOLDER-compress.xml tests-lzf-$FOLDER-uncompress.xml
cp tests-lzf-$FOLDER-compress.xml tests-lzf-$FOLDER-roundtrip.xml
cp tests-lzf-$FOLDER-compress.xml tests-lzf-$FOLDER-all.xml

for file in $FILES; do 
    echo "<testCase name='C:$file' />" >> tests-lzf-$FOLDER-compress.xml
    echo "<testCase name='U:$file' />" >> tests-lzf-$FOLDER-uncompress.xml
    echo "<testCase name='R:$file' />" >> tests-lzf-$FOLDER-roundtrip.xml

    #Do all
    echo "<testCase name='C:$file' />" >> tests-lzf-$FOLDER-all.xml
    echo "<testCase name='U:$file' />" >> tests-lzf-$FOLDER-all.xml
    echo "<testCase name='R:$file' />" >> tests-lzf-$FOLDER-all.xml
done

echo '</testSuite>' >> tests-lzf-$FOLDER-compress.xml
echo '</testSuite>' >> tests-lzf-$FOLDER-uncompress.xml
echo '</testSuite>' >> tests-lzf-$FOLDER-rountrip.xml
echo '</testSuite>' >> tests-lzf-$FOLDER-all.xml





