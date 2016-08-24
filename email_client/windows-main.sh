#!/bin/bash

ip=`route print | egrep "^ +0.0.0.0 +0.0.0.0 +" | gawk 'BEGIN { 
metric=255; ip="0.0.0.0"; } { if ( $5 < metric ) { ip=$4; metric=$5; } } 
END { printf("%s\n",ip); }'` 
echo Current ip is $ip 1>&2 
echo $ip 

sed 's/<IPADDR>/' + $ip + '/g' index.js > index-tmp.js
sed 's/index.js/index-tmp.js/g' index.html > index-tmp.html
echo "writing config for ip addr $ip"
google-chrome index-tmp.html
python3 server.py





