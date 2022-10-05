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

import java.util.*;

final class RequestCache {

    private static final int MINIMUM_CACHE = 60;

    // how long to retain cached data in seconds
    private final int retainCacheSeconds;
    // cache, concurrency safe due to synchronized methods
    private final List<CachedData> cache = new ArrayList<>();

    @SuppressWarnings("SameParameterValue")
    RequestCache(final int retainCacheSeconds){
        this.retainCacheSeconds = Math.min(retainCacheSeconds, MINIMUM_CACHE);
    }

    synchronized final JsonObject getJSON(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){
        // return cached result
        cache.removeIf(CachedData::isExpired);
        for(final CachedData cd : cache)
            if(cd.equals(url, query, headers))
                return cd.getJson();

        // return and cache new result
        final JsonObject json = Requests.getJSON(url, query, headers);
        cache.add(new CachedData(url, query, headers, json));
        return json;
    }

    synchronized final FeedMessage getProtobuf(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){
        // return cached result
        cache.removeIf(CachedData::isExpired);
        for(final CachedData cd : cache)
            if(cd.equals(url, query, headers))
                return cd.getProtobuf();

        // return and cache new result
        final FeedMessage protobuf = Requests.getProtobuf(url, query, headers);
        cache.add(new CachedData(url, query, headers, protobuf));
        return protobuf;
    }

    class CachedData {

        private final String url;
        private final Map<String,String> query;
        private final Map<String,String> headers;

        private final Object object;

        private final long expires;

        CachedData(
            final String url,
            final Map<String,String> query,
            final Map<String,String> headers,
            final Object object
        ){
            this.url     = url;
            this.query   = new HashMap<>(query);
            this.headers = new HashMap<>(headers);

            this.object  = object;

            this.expires = (System.currentTimeMillis() / 1000L) + retainCacheSeconds;
        }

        private boolean isExpired(){
            return (System.currentTimeMillis() / 1000L) > expires;
        }

        private JsonObject getJson(){
            return (JsonObject) object;
        }

        private FeedMessage getProtobuf(){
            return (FeedMessage) object;
        }

        public final boolean equals(final String url, final Map<String,String> query, final Map<String,String> headers){
            return !isExpired() && this.url.equals(url) && this.query.equals(query) && this.headers.equals(headers);
        }

    }

}
