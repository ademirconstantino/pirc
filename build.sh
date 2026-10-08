#!/bin/sh
# Compiles pIRC and creates pirc.jar (requires JDK 17 or newer).
# The Maven build (mvn package) does the same and also runs the tests.
# Run pIRC from this directory (it reads cfg/servers.xml): java -jar pirc.jar
set -e
cd "$(dirname "$0")"

rm -rf build
mkdir -p build/classes
javac --release 17 -encoding UTF-8 -Xlint:all,-serial -d build/classes $(find src/main/java -name '*.java')
cp -R src/main/resources/. build/classes/
printf "SplashScreen-Image: org/aconstantino/pirc/images/splash.gif\n" > build/manifest.txt
jar --create --file pirc.jar --main-class org.aconstantino.pirc.PIRCFrame --manifest build/manifest.txt -C build/classes .

echo "pirc.jar criado. Execute: java -jar pirc.jar"
