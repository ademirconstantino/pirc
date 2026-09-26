#!/bin/sh
# Compiles pIRC and creates pirc.jar
# Run pIRC from this directory (it reads cfg/servers.xml): java -jar pirc.jar
set -e
cd "$(dirname "$0")"

rm -rf build
mkdir -p build/classes
javac -encoding UTF-8 -nowarn -d build/classes $(find org -name '*.java')
cp -R org/aconstantino/pirc/images org/aconstantino/pirc/media build/classes/org/aconstantino/pirc/
jar cfe pirc.jar org.aconstantino.pirc.PIRCFrame -C build/classes .

echo "pirc.jar criado. Execute: java -jar pirc.jar"
