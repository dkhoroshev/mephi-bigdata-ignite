#!/bin/bash

PUBFILE="./publications.txt"
JFILE="./journal.txt"

while read -r string; do
    echo $string
    curl -L -X POST 'http://localhost:8080/publication/create' -H 'Content-Type: application/json' --data-raw "$string"
done < $PUBFILE