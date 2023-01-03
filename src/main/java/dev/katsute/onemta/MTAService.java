/*
 * Copyright (C) 2023 Katsute <https://github.com/Katsute>
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
import java.util.*;

@SuppressWarnings({"SpellCheckingInspection", "Convert2Diamond"})
final class MTAService {

    private final RequestCache cache;

    private final Map<String,String> busAuth, subwayAuth;

    MTAService(final String busToken, final String subwayToken, final int cacheSeconds){
        this.cache = new RequestCache(cacheSeconds);

        busAuth = Collections.singletonMap("key", busToken);
        subwayAuth = Collections.singletonMap("x-api-key", subwayToken);
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

        private final String baseURL = "http://gtfsrt.prod.obanyc.com/";

        @Deprecated
        private final String siriURL = "https://bustime.mta.info/api/2/siri/";

        private BusService(){ }

        final FeedMessage getTripUpdates(){
            return cache.getProtobuf(
                baseURL + "tripUpdates",
                busAuth,
                Collections.emptyMap()
            );
        }

        final FeedMessage getVehiclePositions(){
            return cache.getProtobuf(
                baseURL + "vehiclePositions",
                busAuth,
                Collections.emptyMap()
            );
        }

        final FeedMessage getAlerts(){
            return cache.getProtobuf(
                baseURL + "alerts",
                busAuth,
                Collections.emptyMap()
            );
        }

        @Deprecated
        final JsonObject getVehicle(
            final String token,
            final Integer vehicle,
            final String line,
            final Integer direction,
            final Boolean bus_company
        ){
            return cache.getJSON(
                siriURL + "vehicle-monitoring.json",
                new HashMap<String,String>(){{
                                            put("key", token);
                                            put("version", "2");
                                            put("VehicleMonitoringDetailLevel", "calls");
                    if(vehicle != null)     put("VehicleRef", String.valueOf(vehicle));
                    if(line != null)        put("LineRef", (bus_company == null || !bus_company ? "MTA%20NYCT_" : "MTABC_") + encodeUTF8(line));
                    if(direction != null)   put("DirectionRef", String.valueOf(direction));
                }},
                Collections.emptyMap()
            );
        }

        @Deprecated
        final JsonObject getStop(
            final String token,
            final Integer stop,
            final String line,
            final Integer direction
        ){
            return cache.getJSON(
                siriURL + "stop-monitoring.json",
                new HashMap<String,String>(){{
                                            put("key", token);
                                            put("version", "2");
                                            put("OperatorRef", "MTA");
                    if(stop != null)        put("MonitoringRef", String.valueOf(stop));
                    if(line != null)        put("LineRef", "MTA%20NYCT_" + line);
                    if(direction != null)   put("DirectionRef", String.valueOf(direction));
                }},
                Collections.emptyMap()
            );
        }

    }

    final class SubwayService {

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        private SubwayService(){ }

        final FeedMessage getACE(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-ace",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getBDFM(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-bdfm",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getG(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-g",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getJZ(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-jz",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getNQRW(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-nqrw",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getL(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-l",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage get1234567(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getSI(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-si",
                Collections.emptyMap(),
                subwayAuth
            );
        }

    }

    @SuppressWarnings("FieldCanBeLocal")
    final class LIRRService {
        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        private LIRRService(){ }

        final FeedMessage getLIRR(){
            return cache.getProtobuf(
                baseURL + "lirr%2Fgtfs-lirr",
                Collections.emptyMap(),
                subwayAuth
            );
        }

    }

    @SuppressWarnings("FieldCanBeLocal")
    final class MNRRService {

        private MNRRService(){ }

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        final FeedMessage getMNR(){
            return cache.getProtobuf(
                baseURL + "mnr%2Fgtfs-mnr",
                Collections.emptyMap(),
                subwayAuth
            );
        }

    }

    final class ServiceAlerts {

        private ServiceAlerts(){ }

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        final FeedMessage getBus(

        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fbus-alerts",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getSubway(

        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fsubway-alerts",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getLIRR(

        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Flirr-alerts",
                Collections.emptyMap(),
                subwayAuth
            );
        }

        final FeedMessage getMNR(

        ){
            return cache.getProtobuf(
                baseURL + "camsys%2Fmnr-alerts",
                Collections.emptyMap(),
                subwayAuth
            );
        }

    }

}
