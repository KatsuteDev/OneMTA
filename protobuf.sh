#!/bin/bash

echo ""
echo "⚠ Make sure to include required options in proto file"
echo ""

# option java_package = "dev.katsute.onemta.proto";
# option java_outer_classname = "NYCTSubwayProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime.proto

# option java_package = "dev.katsute.onemta.proto";
# option java_outer_classname = "GTFSRealtimeProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/nyct-subway.proto

cp protobuf/dev/katsute/onemta/*.java src/main/java/dev/katsute/onemta

echo ""
echo "⚠ Make sure to make classes package private abstract and suppress all inspections"
echo ""

read -p "Press any key to exit" -t 10 x