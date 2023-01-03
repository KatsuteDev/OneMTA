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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class RequestCache {

    private static final int MINIMUM_CACHE = 60;

    // how long to retain cached data
    private final int retainCacheMillis;
    // cache
    private final Map<String,CachedData> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("SameParameterValue")
    RequestCache(final int retainCacheSeconds){
        this.retainCacheMillis = Math.max(retainCacheSeconds, MINIMUM_CACHE) * 1000;
    }

    final FeedMessage getProtobuf(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){
        final String key = url + query.hashCode() + headers.hashCode();
        final CachedData cd = cache.get(key);

        if(cd == null || cd.isExpired()){ // check if expired
            synchronized(this){ // lock write
                final CachedData temp = cache.get(key);
                if(temp == null || temp.isExpired()){ // re-check if expired
                    final FeedMessage proto = Requests.getProtobuf(url, query, headers);
                    cache.put(key, new CachedData(proto)); // write
                    return proto;
                }
                return temp.getProtobuf();
            }
        }

        return cd.getProtobuf();
    }

    final class CachedData {

        private final Object object;

        private final long expires;

        CachedData(final Object object){
            this.object  = object;
            this.expires = System.currentTimeMillis() + retainCacheMillis;
        }

        private boolean isExpired(){
            return System.currentTimeMillis() > expires;
        }

        private FeedMessage getProtobuf(){
            return (FeedMessage) object;
        }

    }

}
