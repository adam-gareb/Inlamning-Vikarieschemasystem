#!/bin/bash

PATHSEP=":"
if [[ $OS == "Windows_NT" ]] || [[ $OSTYPE == "cygwin" ]]
then
    PATHSEP=";"
fi

javac -cp www/WEB-INF/lib/org.json.jar:winstone.jar:www/WEB-INF/classes www/WEB-INF/classes/se/yrgo/schedule/servlet/*.java www/WEB-INF/classes/se/yrgo/schedule/format/*.java www/WEB-INF/classes/se/yrgo/schedule/domain/*.java www/WEB-INF/classes/se/yrgo/schedule/data/*.java
java -jar winstone.jar --webroot=www
