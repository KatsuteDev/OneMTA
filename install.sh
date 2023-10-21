#!/bin/bash

version=24.0

if [ -d protobuf ]; then
    exit 0
fi

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

cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-LIRR.proto" "protobuf/gtfs-realtime-LIRR.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-MNR.proto" "protobuf/gtfs-realtime-MNR.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-MTARR.proto" "protobuf/gtfs-realtime-MTARR.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-NYCT.proto" "protobuf/gtfs-realtime-NYCT.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-OneBusAway.proto" "protobuf/gtfs-realtime-OneBusAway.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-crowding.proto" "protobuf/gtfs-realtime-crowding.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime-service-status.proto" "protobuf/gtfs-realtime-service-status.proto"
cp "onebusaway-gtfs-realtime-api/src/main/proto/com/google/transit/realtime/gtfs-realtime.proto" "protobuf/gtfs-realtime.proto"