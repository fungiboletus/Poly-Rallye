#!/bin/sh

ant
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib
java -jar PolyRallye.jar&
sleep 1
xdotool click 1
