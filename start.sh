#!/bin/sh

ant
java -jar PolyRallye.jar&
sleep 1
xdotool click 1
