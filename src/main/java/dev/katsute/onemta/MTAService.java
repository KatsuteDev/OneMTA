/*
 * Copyright (C) 2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package dev.katsute.onemta;

import dev.katsute.onemta.GTFSRealtimeProto.FeedMessage;
import dev.katsute.onemta.Json.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SuppressWarnings("SpellCheckingInspection")
final class MTAService {

    private final RequestCache cache;

    MTAService(final int cacheSeconds){
        this.cache = new RequestCache(cacheSeconds);
    }

    private static String encodeUTF8(final String string){
        try{
            return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
        }catch(final UnsupportedEncodingException ignored){
            return string;
        }
    }

    final BusService bus        = new BusService();
    final SubwayService subway  = new SubwayService();
    final LIRRService lirr      = new LIRRService();
    final MNRRService mnr       = new MNRRService();
    final ServiceAlerts alerts  = new ServiceAlerts();

    final class BusService {

        private final String baseURL = "https://bustime.mta.info/api/2/siri/";

        private BusService(){ }

        final JsonObject getVehicle(
            final String token,
            final Integer vehicle,
            final String line,
            final Integer direction,
            final Boolean bus_company
        ){
            return cache.getJSON(
                baseURL + "vehicle-monitoring.json",
                new HashMap<String,String>(){{
                                            put("key", token);
                                            put("version", "2");
                                            put("VehicleMonitoringDetailLevel", "calls");
                    if(vehicle != null)     put("VehicleRef", String.valueOf(vehicle));
                    if(line != null)        put("LineRef", (bus_company == null || !bus_company ? "MTA%20NYCT_" : "MTABC_") + encodeUTF8(line));
                    if(direction != null)   put("DirectionRef", String.valueOf(direction));
                }},
                new HashMap<>()
            );
        }

        final JsonObject getStop(
            final String token,
            final Integer stop,
            final String line,
            final Integer direction
        ){
            return cache.getJSON(
                baseURL + "stop-monitoring.json",
                new HashMap<String,String>(){{
                                            put("key", token);
                                            put("version", "2");
                                            put("OperatorRef", "MTA");
                    if(stop != null)        put("MonitoringRef", String.valueOf(stop));
                    if(line != null)        put("LineRef", "MTA%20NYCT_" + line);
                    if(direction != null)   put("DirectionRef", String.valueOf(direction));
                }},
                new HashMap<>()
            );
        }

    }

    final class SubwayService {

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        private SubwayService(){ }

        final FeedMessage getACE(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-ace",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getBDFM(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-bdfm",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getG(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-g",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getJZ(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-jz",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getNQRW(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-nqrw",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getL(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-l",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage get1234567(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getSI(final String token){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-si",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

    }

    @SuppressWarnings("FieldCanBeLocal")
    final class LIRRService {
        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        private LIRRService(){ }

        final FeedMessage getLIRR(final String token){
            return cache.getProtobuf(
                baseURL + "lirr%2Fgtfs-lirr",
                new HashMap<>(),
                new HashMap<String,String>() {{
                    put("x-api-key", token);
                }}
            );
        }

    }

    @SuppressWarnings("FieldCanBeLocal")
    final class MNRRService {

        private MNRRService(){ }

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        final FeedMessage getMNR(final String token){
            return cache.getProtobuf(
                baseURL + "mnr%2Fgtfs-mnr",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

    }

    final class ServiceAlerts {

        private ServiceAlerts(){ }

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        final FeedMessage getBus(
            final String token
        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fbus-alerts",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getSubway(
            final String token
        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fsubway-alerts",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getLIRR(
            final String token
        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Flirr-alerts",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

        final FeedMessage getMNR(
            final String token
        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fmnr-alerts",
                new HashMap<>(),
                new HashMap<String,String>(){{
                    put("x-api-key", token);
                }}
            );
        }

    }

}
