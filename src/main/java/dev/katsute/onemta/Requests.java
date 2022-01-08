/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
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

import com.google.protobuf.ExtensionRegistry;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class Requests {

    static Json.JsonObject getJSON(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){

        headers.put("Accept", "application/json; charset=UTF-8");

        HttpURLConnection conn = null;
        try{
            conn = getConnection(url, query, headers);
            try(final BufferedReader IN = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
                String buffer;
                final StringBuilder OUT = new StringBuilder();
                while((buffer = IN.readLine()) != null)
                    OUT.append(buffer);
                return (Json.JsonObject) Json.parse(OUT.toString());
            }catch(IOException e){
                return (Json.JsonObject) Json.parse("{}");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(conn != null)
                conn.disconnect();
        }
        return null;
    }

    private static final ExtensionRegistry registry = ExtensionRegistry.newInstance();

    static{
        registry.add(NYCTSubwayProto.nyctFeedHeader);
        registry.add(NYCTSubwayProto.nyctTripDescriptor);
        registry.add(NYCTSubwayProto.nyctStopTimeUpdate);
    }

    static GTFSRealtimeProto.FeedMessage getProtobuf(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ){
        headers.put("Accept", "application/x-google-protobuf");

        HttpURLConnection conn = null;
        try{
            conn = getConnection(url, query, headers);
            try(final InputStream IN = conn.getInputStream()){
                return GTFSRealtimeProto.FeedMessage.parseFrom(IN, registry);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(conn != null)
                conn.disconnect();
        }
        return null;
    }

    // [{}|\\^\[\]`]
    private static final Pattern blockedURI = Pattern.compile("[{}|\\\\^\\[\\]`]");

    private static final URIEncoder encoder = new URIEncoder();

    private static HttpURLConnection getConnection(
        final String url,
        final Map<String,String> query,
        final Map<String,String> headers
    ) throws IOException{
        final String URL =
            url +
            // path args
            (query.isEmpty() ? "" : '?' + query.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("&"))); // query

        final HttpURLConnection conn = (HttpURLConnection) URI
            .create(Regex9.replaceAll(URL, blockedURI.matcher(URL), encoder))
            .toURL()
            .openConnection();

        headers.put("Cache-Control", "no-cache, no-store, must-revalidate");

        for(final Map.Entry<String, String> entry : headers.entrySet())
            conn.setRequestProperty(entry.getKey(), entry.getValue());

        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);

        conn.setRequestMethod("GET");

        return conn;
    }

    private static class URIEncoder implements Function<MatchResult,String> {

        @Override
        public final String apply(final MatchResult matchResult){
            final char ch = matchResult.group().charAt(0);
            switch(ch){
                case '{':
                    return "%7B";
                case '}':
                    return "%7D";
                case '|':
                    return "%7C";
                case '\\':
                    return "%5C";
                case '^':
                    return "%5E";
                case '[':
                    return "%5B";
                case ']':
                    return "%5D";
                case '`':
                    return "%60";
                default:
                    return matchResult.group(0);
            }
        }

    }

}
