package dev.katsute.onemta;

abstract class MTAService {

    @SuppressWarnings("DefaultAnnotationParam")
    interface MTABusService {

        String baseURL = "http://bustime.mta.info/api/siri/";

        static MTABusService create(){
            return APICall.create(baseURL, MTABusService.class);
        }

        @APIStruct.Endpoint(method="GET", value="vehicle-monitoring.json")
        APIStruct.Response<Json.JsonObject> getVehicle(
            @APIStruct.Query("key")           final String token,
            @APIStruct.Query("VehicleRef")    final Integer vehicle,
            @APIStruct.Query("LineRef")       final String line,
            @APIStruct.Query("DirectionRef")  final Integer direction
        );

        @APIStruct.Endpoint(method="GET", value="stop-monitoring.json")
        APIStruct.Response<Json.JsonObject> getStop(
            @APIStruct.Query("key")           final String token,
            @APIStruct.Query("MonitoringRef") final Integer stop,
            @APIStruct.Query("LineRef")       final String line,
            @APIStruct.Query("DirectionRef")  final Integer direction
        );

    }

    @SuppressWarnings({"DefaultAnnotationParam", "SpellCheckingInspection"})
    interface MTASubwayService {

        String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        static MTASubwayService create(){
            return APICall.create(baseURL, MTASubwayService.class);
        }

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-ace")
        APIStruct.Response<Json.JsonObject> getACE(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-bdfm")
        APIStruct.Response<Json.JsonObject> getBDFM(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-g")
        APIStruct.Response<Json.JsonObject> getG(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-jz")
        APIStruct.Response<Json.JsonObject> getJZ(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-nqrw")
        APIStruct.Response<Json.JsonObject> getNQRW(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-l")
        APIStruct.Response<Json.JsonObject> getL(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs")
        APIStruct.Response<Json.JsonObject> get1234567(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-si")
        APIStruct.Response<Json.JsonObject> getSI(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-lirr")
        APIStruct.Response<Json.JsonObject> getLIRR(
            @APIStruct.Header("x-api-key") final String token
        );

        @APIStruct.Protobuf
        @APIStruct.Endpoint(method="GET", value="nyct%2Fgtfs-mnr")
        APIStruct.Response<Json.JsonObject> getMNRR(
            @APIStruct.Header("x-api-key") final String token
        );

    }

}
