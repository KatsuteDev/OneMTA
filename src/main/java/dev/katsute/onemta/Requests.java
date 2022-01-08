package dev.katsute.onemta;

import com.google.protobuf.ExtensionRegistry;
import dev.katsute.onemta.exception.StaticInitializerException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class Requests {

    static Json.JsonObject getJSON(
        final String url,
        final Map<String,String> query,
        final Map<String,String> requestProperty
    ){
        try(final BufferedReader IN = new BufferedReader(new InputStreamReader(getInputStream(url, query, requestProperty)))){
            String buffer;
            final StringBuilder OUT = new StringBuilder();
            while((buffer = IN.readLine()) != null)
                OUT.append(buffer);
            final String body = OUT.toString();
            return (Json.JsonObject) Json.parse(body);
        }catch(IOException e){
            return (Json.JsonObject) Json.parse("{}");
        }
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
        final Map<String,String> requestProperty
    ){
        try(final InputStream IN = getInputStream(url, query, requestProperty)){
            return GTFSRealtimeProto.FeedMessage.parseDelimitedFrom(IN, registry);
        }catch(IOException e){
            return null;
        }
    }

    // [{}|\\^\[\]`]
    private static final Pattern blockedURI = Pattern.compile("[{}|\\\\^\\[\\]`]");

    private static final URIEncoder encoder = new URIEncoder();

    private static InputStream getInputStream(
        final String url,
        final Map<String,String> query,
        final Map<String,String> requestProperty
    ){
        final String URL =
            url +
            // path args
            (query.isEmpty() ? "" : '?' + query.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("&"))); // query

        return null;
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
