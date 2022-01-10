#!/bin/bash

# https://github.com/OneBusAway/onebusaway-gtfs-realtime-api/tree/master/src/main/proto/com/google/transit/realtime

echo ""
echo "⚠ Make sure to include required options in proto file"
echo ""

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "GTFSRealtimeProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "NYCTSubwayProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-NYCT.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "MTARRProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-MTARR.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "LIRRProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-LIRR.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "MNRRProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-MNR.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "CrowdingProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-crowding.proto

# import "gtfs-realtime.proto";
# option java_package = "dev.katsute.onemta";
# option java_outer_classname = "ServiceStatusProto";
# option optimize_for = CODE_SIZE;

protobuf/bin/protoc -I=protobuf --java_out=protobuf  protobuf/gtfs-realtime-service-status.proto

cp protobuf/dev/katsute/onemta/*.java src/main/java/dev/katsute/onemta

echo ""
echo "⚠ Make sure to make classes package private abstract and suppress all inspections"
echo ""

read -p "Press any key to exit" -t 10 x