#!/bin/bash
echo "This is from execute jar . sh"
echo "Script executed from: ${PWD}"

BASEDIR=$(dirname $0)
echo "Script location: ${BASEDIR}"
java -jar app.jar


