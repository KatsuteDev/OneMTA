/*
 * Copyright (C) 2025 Katsute <https://github.com/Katsute>
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
final class MTAService {

    private final RequestCache cache;

    private final Map<String,String> busAuth;

    MTAService(final String busToken, final int cacheSeconds){
        this.cache = new RequestCache(cacheSeconds);
        busAuth = Collections.singletonMap("key", busToken);
    }

    private static String encodeUTF8(final String string){
        try{
            return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
        }catch(final UnsupportedEncodingException ignored){
            return string;
        }
    }

    final BusService bus = new BusService();
    final SubwayService subway = new SubwayService();
    final LIRRService lirr = new LIRRService();
    final MNRRService mnr = new MNRRService();
    final ServiceAlerts alerts = new ServiceAlerts();

    final class BusService {

        private final String baseURL = "http://gtfsrt.prod.obanyc.com/";

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

    }

    final class SubwayService {

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        private SubwayService(){ }

        final FeedMessage getACE(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-ace",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getBDFM(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-bdfm",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getG(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-g",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getJZ(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-jz",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getNQRW(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-nqrw",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getL(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-l",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage get1234567(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getSI(){
            return cache.getProtobuf(
                baseURL + "nyct%2Fgtfs-si",
                Collections.emptyMap(),
                Collections.emptyMap()
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
                Collections.emptyMap()
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
                Collections.emptyMap()
            );
        }

    }

    final class ServiceAlerts {

        private ServiceAlerts(){ }

        private final String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

        final FeedMessage getBus(){
            return cache.getProtobuf(
                baseURL + "camsys%2Fbus-alerts",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getSubway(){
            return cache.getProtobuf(
                baseURL + "camsys%2Fsubway-alerts",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getLIRR(){
            return cache.getProtobuf(
                baseURL + "camsys%2Flirr-alerts",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

        final FeedMessage getMNR(){
            return cache.getProtobuf(
                baseURL + "camsys%2Fmnr-alerts",
                Collections.emptyMap(),
                Collections.emptyMap()
            );
        }

    }

}