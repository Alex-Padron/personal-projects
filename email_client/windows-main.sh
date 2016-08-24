#!/bin/bash

IP=`route print | egrep "^ +0.0.0.0 +0.0.0.0 +" | gawk 'BEGIN { 
metric=255; ip="0.0.0.0"; } { if ( $5 < metric ) { ip=$4; metric=$5; } } 
END { printf("%s\n",ip); }'` 
echo Updating temp files with ip $IP 1>&2 

sed 's/<IPADDR>/'$IP'/g' index.js > index-tmp.js
sed 's/index.js/index-tmp.js/g' index.html > index-tmp.html
cygstart chrome
cygstart chrome index-tmp.html
python3 server.py





