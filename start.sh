#!/bin/bash

PUBFILE="./publications.txt"
JFILE="./journal.txt"

[[ ! -f ${PUBFILE} ]] && python generator.py
[[ ! -f ${JFILE} ]] && python generator.py

[[ ! -f target/bigdata.ignitecompute-1.0-jar-with-dependencies.jar ]] && mvn compile package

java -jar target/bigdata.ignitecompute-1.0-jar-with-dependencies.jar ${PUBFILE} ${JFILE}