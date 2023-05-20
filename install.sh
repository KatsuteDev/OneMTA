#!/bin/bash

version=23.1

mkdir protobuf

if [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
    echo "Installing protobuf $version for windows"
    curl -L -k -o protobuf/protobuf.zip "https://github.com/protocolbuffers/protobuf/releases/download/v$version/protoc-$version-win64.zip"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    echo "Installing protobuf $version for ubuntu"
    apt-get update
    apt-get install unzip
    curl -L -k -o protobuf/protobuf.zip "https://github.com/protocolbuffers/protobuf/releases/download/v$version/protoc-$version-linux-x86_64.zip"
elif [ "$(uname)" == "Darwin" ]; then
    echo "Installing protobuf $version for OSX"
    curl -L -k -o protobuf/protobuf.zip "https://github.com/protocolbuffers/protobuf/releases/download/v$version/protoc-$version-osx-x86_64.zip"
fi

unzip protobuf/protobuf.zip -d protobuf
rm protobuf/protobuf.zip -f
rm protobuf/readme.txt -f

curl -L -k -o protobuf/gtfs-realtime-LIRR.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-LIRR.proto"
curl -L -k -o protobuf/gtfs-realtime-MNR.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-MNR.proto"
curl -L -k -o protobuf/gtfs-realtime-MTARR.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-MTARR.proto"
curl -L -k -o protobuf/gtfs-realtime-NYCT.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-NYCT.proto"
curl -L -k -o protobuf/gtfs-realtime-OneBusAway.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-OneBusAway.proto"
curl -L -k -o protobuf/gtfs-realtime-crowding.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-crowding.proto"
curl -L -k -o protobuf/gtfs-realtime-service-status.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime-service-status.proto"
curl -L -k -o protobuf/gtfs-realtime.proto "https://raw.githubusercontent.com/OneBusAway/onebusaway-gtfs-realtime-api/master/src/main/proto/com/google/transit/realtime/gtfs-realtime.proto"