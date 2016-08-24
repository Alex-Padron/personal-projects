#!/bin/bash
ip="$(C:\> ipconfig | grep -i 'Ip Address' | grep -v ': 19' | grep -v ': 0.' | grep -v ': 10.' | cut -b44-)"
sed 's/<IPADDR>/ip/g' index.js > index-tmp.js
sed 's/index.js/index-tmp.js/g' index.html > index-tmp.html
echo "writing config for ip addr $ip"
google-chrome index-tmp.html
python3 server.py





