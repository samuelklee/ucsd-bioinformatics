#!/bin/bash

EXT_LIB=/usr/share/java/commons-lang3.jar:/usr/share/java/commons-math.jar

cd src/main/java
javac -cp .:$EXT_LIB ucsd/$1.java
rm solution.txt
java -cp .:$EXT_LIB ucsd.$1 < /home/slee/working/ucsd-bioinformatics/src/test/resources/$2.txt > solution.txt
cat solution.txt
rm ucsd/*.class
cd ..