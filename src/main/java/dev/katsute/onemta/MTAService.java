package dev.katsute.onemta;

import dev.katsute.onemta.GTFSRealtimeProto.FeedMessage;
import dev.katsute.onemta.Json.JsonObject;

import java.util.HashMap;

abstract class MTAService {

    interface BusService {

        String baseURL = "http://bustime.mta.info/api/siri/";

        static JsonObject getVehicle(
            final String token,
            final Integer vehicle,
            final String line,
            final Integer direction
        ){
            return Requests.getJSON(
                baseURL + "vehicle-monitoring.json",
                new HashMap<>(){{
                    put("key", token);
                    if(vehicle != null) put("VehicleRef", String.valueOf(vehicle));
                    if(line != null) put("LineRef", line);
                    if(direction != null) put("DirectionRef", String.valueOf(direction));
                }},
                new HashMap<>()
            );
        }

        static JsonObject getStop(
            final String token,
            final Integer stop,
            final String line,
            final Integer direction
        ){
            return Requests.getJSON(
                baseURL + "stop-monitoring.json",
                new HashMap<>(){{
                    put("key", token);
                    if(stop != null) put("MonitoringRef", String.valueOf(stop));
                    if(line != null) put("LineRef", line);
                    if(direction != null) put("DirectionRef", String.valueOf(direction));
                }},
                new HashMap<>()
            );
        }

    }

    @SuppressWarnings({"SpellCheckingInspection"})
    interface SubwayService {

        String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        static FeedMessage getACE(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-ace",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getBDFM(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-bdfm",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getG(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-g",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getJZ(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-jz",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getNQRW(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-nqrw",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getL(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-l",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage get1234567(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getSI(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-si",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getLIRR(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-lirr",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

        static FeedMessage getMNRR(final String token){
            return Requests.getProtobuf(
                baseURL + "nyct%2Fgtfs-mnr",
                new HashMap<>(),
                new HashMap<>(){{
                    put("x-api-key", token);
                }}
            );
        }

    }

}
