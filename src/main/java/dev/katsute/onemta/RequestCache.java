package dev.katsute.onemta;

import dev.katsute.onemta.GTFSRealtimeProto.FeedMessage;
import dev.katsute.onemta.Json.JsonObject;

import java.util.*;

final class RequestCache {

    static JsonObject getJSON(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){

    }

    static FeedMessage getProtobuf(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){

    }

    class CachedData {

        private final String url;
        private final Map<String,String> query;
        private final Map<String,String> headers;
        private final Object data;
        private final long expires;

        CachedData(
            final String url,
            final Map<String,String> query,
            final Map<String,String> headers,
            final JsonObject data
        ){
            this(url, query, headers, (Object) data);
        }

        CachedData(
            final String url,
            final Map<String,String> query,
            final Map<String,String> headers,
            final FeedMessage data
        ){
            this(url, query, headers, (Object) data);
        }

        CachedData(
            final String url,
            final Map<String,String> query,
            final Map<String,String> headers,
            final Object data
        ){
            this.url = url;
            this.query = new HashMap<>(query);
            this.headers = new HashMap<>(headers);

            this.data = data;

            this.expires = (System.currentTimeMillis() / 1000L) + 30;
        }

        final boolean isExpired(){
            return (System.currentTimeMillis() / 1000L) > expires;
        }

        final JsonObject getJson(){
            return (JsonObject) data;
        }

        final FeedMessage getProtobuf(){
            return (FeedMessage) data;
        }

        @Override
        public boolean equals(final Object o){
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            if(isExpired()) return false;

            final CachedData that = (CachedData) o;

            return url.equals(that.url) && query.equals(that.query) && headers.equals(that.headers);
        }

    }

}
