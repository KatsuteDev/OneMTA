#!/bin/bash

# https://github.com/OneBusAway/onebusaway-gtfs-realtime-api/tree/master/src/main/proto/com/google/transit/realtime

if [ ! -d protobuf ]; then
    echo "Missing protobuf directory, run install.sh first"
    exit 0
fi

echo "Compiling proto files for Java, do not run this script more than once"

sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "GTFSRealtimeProto";\n\1/' protobuf/gtfs-realtime.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/GTFSRealtimeProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/GTFSRealtimeProto.java
cat -s protobuf/dev/katsute/onemta/GTFSRealtimeProto.java > protobuf/dev/katsute/onemta/GTFSRealtimeProto.temp.java
mv protobuf/dev/katsute/onemta/GTFSRealtimeProto.temp.java protobuf/dev/katsute/onemta/GTFSRealtimeProto.java
truncate -s -1 protobuf/dev/katsute/onemta/GTFSRealtimeProto.java

# OneBusAway

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-OneBusAway.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-OneBusAway.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "OneBusAwayProto";\n\1/' protobuf/gtfs-realtime-OneBusAway.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-OneBusAway.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-OneBusAway.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/OneBusAwayProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/OneBusAwayProto.java
cat -s protobuf/dev/katsute/onemta/OneBusAwayProto.java > protobuf/dev/katsute/onemta/OneBusAwayProto.temp.java
mv protobuf/dev/katsute/onemta/OneBusAwayProto.temp.java protobuf/dev/katsute/onemta/OneBusAwayProto.java
truncate -s -1 protobuf/dev/katsute/onemta/OneBusAwayProto.java

# NYCT

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-NYCT.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-NYCT.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "NYCTSubwayProto";\n\1/' protobuf/gtfs-realtime-NYCT.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-NYCT.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-NYCT.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/NYCTSubwayProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/NYCTSubwayProto.java
cat -s protobuf/dev/katsute/onemta/NYCTSubwayProto.java > protobuf/dev/katsute/onemta/NYCTSubwayProto.temp.java
mv protobuf/dev/katsute/onemta/NYCTSubwayProto.temp.java protobuf/dev/katsute/onemta/NYCTSubwayProto.java
truncate -s -1 protobuf/dev/katsute/onemta/NYCTSubwayProto.java

# MTARR

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-MTARR.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-MTARR.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "MTARRProto";\n\1/' protobuf/gtfs-realtime-MTARR.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-MTARR.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-MTARR.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/MTARRProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/MTARRProto.java
cat -s protobuf/dev/katsute/onemta/MTARRProto.java > protobuf/dev/katsute/onemta/MTARRProto.temp.java
mv protobuf/dev/katsute/onemta/MTARRProto.temp.java protobuf/dev/katsute/onemta/MTARRProto.java
truncate -s -1 protobuf/dev/katsute/onemta/MTARRProto.java

# LIRR

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-LIRR.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-LIRR.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "LIRRProto";\n\1/' protobuf/gtfs-realtime-LIRR.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-LIRR.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-LIRR.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/LIRRProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/LIRRProto.java
cat -s protobuf/dev/katsute/onemta/LIRRProto.java > protobuf/dev/katsute/onemta/LIRRProto.temp.java
mv protobuf/dev/katsute/onemta/LIRRProto.temp.java protobuf/dev/katsute/onemta/LIRRProto.java
truncate -s -1 protobuf/dev/katsute/onemta/LIRRProto.java

# MNR

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-MNR.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-MNR.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "MNRRProto";\n\1/' protobuf/gtfs-realtime-MNR.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-MNR.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-MNR.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/MNRRProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/MNRRProto.java
cat -s protobuf/dev/katsute/onemta/MNRRProto.java > protobuf/dev/katsute/onemta/MNRRProto.temp.java
mv protobuf/dev/katsute/onemta/MNRRProto.temp.java protobuf/dev/katsute/onemta/MNRRProto.java
truncate -s -1 protobuf/dev/katsute/onemta/MNRRProto.java

# crowding

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-crowding.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-crowding.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "CrowdingProto";\n\1/' protobuf/gtfs-realtime-crowding.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-crowding.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-crowding.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/CrowdingProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/CrowdingProto.java
cat -s protobuf/dev/katsute/onemta/CrowdingProto.java > protobuf/dev/katsute/onemta/CrowdingProto.temp.java
mv protobuf/dev/katsute/onemta/CrowdingProto.temp.java protobuf/dev/katsute/onemta/CrowdingProto.java
truncate -s -1 protobuf/dev/katsute/onemta/CrowdingProto.java

# service status

sed -i -E 's/(^import ")(.*)("; *$)/\1gtfs-realtime.proto\3/i;' protobuf/gtfs-realtime-service-status.proto
sed -i -E 's/(^option java_package = ")(.*)("; *$)/\1dev.katsute.onemta\3/i' protobuf/gtfs-realtime-service-status.proto
sed -i -E 's/(^package transit_realtime;$)/option java_outer_classname = "ServiceStatusProto";\n\1/' protobuf/gtfs-realtime-service-status.proto
sed -i -E 's/(^package transit_realtime;$)/option optimize_for = CODE_SIZE;\n\1/' protobuf/gtfs-realtime-service-status.proto

protobuf/bin/protoc -I=protobuf --java_out=protobuf protobuf/gtfs-realtime-service-status.proto

sed -i -E 's/^public final class /@SuppressWarnings("all")\nabstract class /' protobuf/dev/katsute/onemta/ServiceStatusProto.java
sed -i -E 's/ *$//' protobuf/dev/katsute/onemta/ServiceStatusProto.java
cat -s protobuf/dev/katsute/onemta/ServiceStatusProto.java > protobuf/dev/katsute/onemta/ServiceStatusProto.temp.java
mv protobuf/dev/katsute/onemta/ServiceStatusProto.temp.java protobuf/dev/katsute/onemta/ServiceStatusProto.java
truncate -s -1 protobuf/dev/katsute/onemta/ServiceStatusProto.java

# copy

cp protobuf/dev/katsute/onemta/*.java src/main/java/dev/katsute/onemta

echo "Finished compiling and formatting proto files"