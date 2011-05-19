set PATH=../../lib/;../../jre/bin/

rem attention, pas d'expace derrière le =
java -Djava.library.path=../ressources/lib -jar SI_VOX.jar

rem utilisation muette pour faire une démonstration
rem java t2s/Main -f mot.txt tutu 
rem crée tutu.wav et tutu.pho