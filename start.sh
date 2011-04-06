#!/bin/sh

ant
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib
(sleep 2 ; xdotool click 1) &
java -jar PolyRallye.jar $*
