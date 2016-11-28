:: Creates database and its tables

@echo off

set DERBY_RUN_JAR_PATH="%~1db\lib\derbyrun.jar"

java -jar %DERBY_RUN_JAR_PATH% ij "ij.txt"