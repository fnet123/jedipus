#!/bin/bash

name=$1
serverPort=$2

startPort=$3
numNodes=$4
endPort=$((startPort + numNodes - 1))

docker ps -a -q -f name="$name" | xargs docker rm -f

docker run --name="$name"-"$serverPort" -d --publish="$serverPort":"$serverPort" jamespedwards42/alpine-redis-testing:unstable redis-server "$serverPort" 1 --requirepass 42 --save \"\" --repl-diskless-sync yes &

docker run -d --name="$name-cluster" -p "$startPort-$endPort:$startPort-$endPort" jamespedwards42/alpine-redis-testing:unstable redis-server "$startPort" "$numNodes" --cluster-enabled yes --cluster-announce-ip '127.0.0.1'

wait

exit 0
