#!/bin/bash

ip="$(ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}')"
sed 's/<IPADDR>/'$ip'/g' index.js > index-tmp.js
sed 's/index.js/index-tmp.js/g' index.html > index-tmp.html
echo "writing config for ip addr $ip"
google-chrome index-tmp.html
python3 server.py

