#!/bin/bash
workspaceDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"/..
tempFile=/temp/javaFiles.txt

srcDir=$workspaceDir/src
binDir=$workspaceDir/bin
javac=$jdk6_64/bin/javac

echo "Clearing old classes"
rm --recursive --force $binDir/sk

echo "Recreating directory tree"
if ! [ -d $binDir ]; then
    mkdir $binDir
fi

echo "Locating java files"
find $srcDir -name *.java > $tempFile

echo "Compiling java files"
$javac -cp $workspaceDir/lib/MyLibrary.jar:$binDir -d $binDir @$srcDir
