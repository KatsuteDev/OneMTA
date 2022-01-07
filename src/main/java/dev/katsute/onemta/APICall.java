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
import dev.katsute.onemta.exception.ReflectedClassException;
import dev.katsute.onemta.exception.StaticInitializerException;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.katsute.onemta.APIStruct.*;

/**
 * Represents an API call.
 */
@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
class APICall {

    private final String method;
    private final String baseURL;
    private final String path;

    /**
     * API call builder.
     *
     * @param method request method
     * @param baseURL base url
     * @param path path
     */
    APICall(final String method, final String baseURL, final String path){
        this.method     = method;
        this.baseURL    = baseURL;
        this.path       = path;
    }

    /**
     * API calls from annotated interface method.
     *
     * @param baseURL base url
     * @param method method
     * @param args method arguments
     *
     * @see APIStruct
     */
    APICall(final String baseURL, final Method method, final Object... args){
        this.baseURL = baseURL;

        final Endpoint endpoint = method.getAnnotation(Endpoint.class);
        if(endpoint != null){
            this.method = endpoint.method();
            path = endpoint.value();
        }else{
            this.method = "GET";
            this.path = "";
        }

        if(method.getAnnotation(FormUrlEncoded.class) != null)
            formUrlEncoded = true;
        if(method.getAnnotation(Protobuf.class) != null)
            protobuf = true;

        for(int i = 0, size = method.getParameterAnnotations().length; i < size; i++){
            final Object arg = args[i];
            for(final Annotation annotation : method.getParameterAnnotations()[i]){
                if(arg != null){
                    final Class<? extends Annotation> type = annotation.annotationType();
                    if(type == Path.class)
                        withPathVar(((Path) annotation).value(), arg, ((Path) annotation).encoded());
                    else if(type == Header.class)
                        withHeader(((Header) annotation).value(), Objects.toString(arg));
                    else if(type == Query.class)
                        withQuery(((Query) annotation).value(), arg, ((Query) annotation).encoded());
                    else if(type == APIStruct.Field.class)
                        withField(((APIStruct.Field) annotation).value(), arg, ((APIStruct.Field) annotation).encoded());
                }
            }
        }
    }

    private final Map<String,String> headers = new HashMap<>();

    // \{(.*?)\}
    @SuppressWarnings("RegExpRedundantEscape") // android requires this syntax (#133)
    private static final Pattern pathArg = Pattern.compile("\\{(.*?)\\}");

    private final Map<String,String> pathVars = new HashMap<>();
    private final Map<String,String> queries  = new HashMap<>();

    private boolean formUrlEncoded = false;
    private boolean protobuf       = false;
    private final Map<String,String> fields = new HashMap<>();

    final APICall withHeader(final String header, final String value){
        if(value == null)
            headers.remove(header);
        else
            headers.put(header, value);
        return this;
    }

    final APICall withPathVar(final String pathVar, final String value){
        return withPathVar(pathVar, value, false);
    }

    final APICall withPathVar(final String pathVar, final Object value, final boolean encoded){
        if(value == null)
            pathVars.remove(pathVar);
        else
            pathVars.put(pathVar, encoded ? Objects.toString(value) : Java9.URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    final APICall withQuery(final String query, final Object value){
        return withQuery(query, value, false);
    }

    final APICall withQuery(final String query, final Object value, final boolean encoded){
        if(value == null)
            queries.remove(query);
        else
            queries.put(query, encoded ? Objects.toString(value) : Java9.URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    final APICall formUrlEncoded(){
        return formUrlEncoded(true);
    }

    final APICall formUrlEncoded(final boolean formUrlEncoded){
        this.formUrlEncoded = formUrlEncoded;
        return this;
    }

    final APICall withField(final String field, final Object value){
        return withField(field, value, false);
    }

    final APICall withField(final String field, final Object value, final boolean encoded){
        if(value == null)
            fields.remove(field);
        else
            fields.put(field, encoded ? Objects.toString(value) : Java9.URLEncoder.encode(Objects.toString(value), StandardCharsets.UTF_8));
        return this;
    }

    // protobuf

    final APICall protobuf(){
        return protobuf(true);
    }

    final APICall protobuf(final boolean protobuf){
        this.protobuf = true;
        return this;
    }

    private static final ExtensionRegistry registry = ExtensionRegistry.newInstance();

    static{
        try{
            registry.add(NYCTSubwayProto.nyctFeedHeader);
            registry.add(NYCTSubwayProto.nyctTripDescriptor);
            registry.add(NYCTSubwayProto.nyctStopTimeUpdate);
        }catch(final Throwable e){
            throw new StaticInitializerException("Failed to initialize ExtensionRegistry, please report this to the maintainers of OneMTA", e);
        }
    }

    // call

    private static final boolean useNetHttp;

    @SuppressWarnings("FieldCanBeLocal")
    private static class JDK11 {

        private static Class<?> HttpRequest;
            private static Method HttpRequest_NewBuilder;

        private static Class<?> HttpRequestBuilder;
            private static Method HttpRequestBuilder_URI;
            private static Method HttpRequestBuilder_Method;
            private static Method HttpRequestBuilder_Header;
                private static Method BodyPublishers_NoBody;
                private static Method BodyPublishers_StringBody;
            private static Method HttpRequestBuilder_Build;

        private static Class<?> HttpClientBuilder;
            private static Method HttpClientBuilder_ConnectTimeout;
            private static Method HttpClientBuilder_Build;

        private static Class<?> HttpClient;
            private static Method HttpClient_NewBuilder;
            private static Method HttpClient_Send;
                private static Method BodyHandlers_StringBody;

        private static Method HttpResponse_Body;
        private static Method HttpResponse_Code;

        static {
            if(useNetHttp)
                try{
                    HttpRequest = Class.forName("java.net.http.HttpRequest");
                        HttpRequest_NewBuilder = HttpRequest.getDeclaredMethod("newBuilder");
                    HttpRequestBuilder = Class.forName("java.net.http.HttpRequest$Builder");
                        HttpRequestBuilder_URI = HttpRequestBuilder.getDeclaredMethod("uri", URI.class);
                        HttpRequestBuilder_Method = HttpRequestBuilder.getDeclaredMethod("method", String.class, Class.forName("java.net.http.HttpRequest$BodyPublisher"));
                        HttpRequestBuilder_Header = HttpRequestBuilder.getDeclaredMethod("header", String.class, String.class);
                            BodyPublishers_NoBody =  Class.forName("java.net.http.HttpRequest$BodyPublishers").getDeclaredMethod("noBody");
                            BodyPublishers_StringBody =  Class.forName("java.net.http.HttpRequest$BodyPublishers").getDeclaredMethod("ofString", String.class);
                        HttpRequestBuilder_Build = HttpRequestBuilder.getDeclaredMethod("build");
                    HttpClientBuilder = Class.forName("java.net.http.HttpClient$Builder");
                        HttpClientBuilder_ConnectTimeout = HttpClientBuilder.getDeclaredMethod("connectTimeout", Duration.class);
                        HttpClientBuilder_Build = HttpClientBuilder.getDeclaredMethod("build");
                    HttpClient = Class.forName("java.net.http.HttpClient");
                        HttpClient_NewBuilder = HttpClient.getDeclaredMethod("newBuilder");
                        HttpClient_Send = HttpClient.getDeclaredMethod("send", Class.forName("java.net.http.HttpRequest"), Class.forName("java.net.http.HttpResponse$BodyHandler"));
                            BodyHandlers_StringBody =  Class.forName("java.net.http.HttpResponse$BodyHandlers").getDeclaredMethod("ofString", Charset.class);

                    HttpResponse_Body = Class.forName("java.net.http.HttpResponse").getDeclaredMethod("body");
                    HttpResponse_Code = Class.forName("java.net.http.HttpResponse").getDeclaredMethod("statusCode");
                }catch(final ClassNotFoundException | NoSuchMethodException e){
                    throw new StaticInitializerException("Failed to initialize HttpClient, please report this to the maintainers of OneMTA", e);
                }
        }

    }

    // try to initialize HTTPUrlConnection
    static {
        final String version = System.getProperty("java.version");
        useNetHttp = (version != null ? Integer.parseInt(version.contains(".") ? version.substring(0, version.indexOf(".")) : version) : 0) >= 11;
    }

    // [{}|\\^\[\]`]
    private static final Pattern blockedURI = Pattern.compile("[{}|\\\\^\\[\\]`]");

    private static final URIEncoder encoder = new URIEncoder();

    @SuppressWarnings("RedundantThrows")
    private APIStruct.Response<String> call() throws IOException, InterruptedException{
        final String URL =
            baseURL +
            Java9.Matcher.replaceAll(path, pathArg.matcher(path), result -> pathVars.get(result.group(1))) + // path args
            (queries.isEmpty() ? "" : '?' + queries.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("&"))); // query

        final String data = fields.isEmpty() ? "" : fields.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("&"));

        String body;
        int code;

        if(useNetHttp)
            try{
                // final HttpRequest.Builder request = HttpRequest.newBuilder();
                final Object HttpRequestBuilder_Instance = JDK11.HttpRequest_NewBuilder.invoke(null);

                // request.uri(URI.create(blockedURI.matcher(URL).replaceAll(encoder)));
                JDK11.HttpRequestBuilder_URI
                    .invoke(HttpRequestBuilder_Instance,
                        URI.create(Java9.Matcher.replaceAll(URL, blockedURI.matcher(URL),encoder))
                    );
                // request.method(method, HttpRequest.BodyPublishers.noBody());
                JDK11.HttpRequestBuilder_Method
                    .invoke(HttpRequestBuilder_Instance,
                        method,
                        JDK11.BodyPublishers_NoBody.invoke(null)
                    );

                for(final Map.Entry<String, String> entry : headers.entrySet())
                    // request.header(entry.getKey(), entry.getValue());
                    JDK11.HttpRequestBuilder_Header
                        .invoke(HttpRequestBuilder_Instance,
                            entry.getKey(),
                            entry.getValue()
                        );

                // request.header("Cache-Control", "no-cache, no-store, must-revalidate");
                JDK11.HttpRequestBuilder_Header
                    .invoke(HttpRequestBuilder_Instance,
                        "Cache-Control",
                        "no-cache, no-store, must-revalidate"
                    );

                // request.header("Accept", "application/json; charset=UTF-8");
                JDK11.HttpRequestBuilder_Header
                    .invoke(HttpRequestBuilder_Instance,
                        "Accept",
                        "application/json; charset=UTF-8"
                    );

                if(formUrlEncoded){
                    // request.header("Content-Type", protobuf ? "application/x-google-protobuf" : "application/x-www-form-urlencoded");
                    JDK11.HttpRequestBuilder_Header
                        .invoke(HttpRequestBuilder_Instance,
                            "Content-Type",
                            protobuf ? "application/x-google-protobuf" : "application/x-www-form-urlencoded"
                        );

                    // request.method(method, HttpRequest.BodyPublishers.ofString(data));
                    JDK11.HttpRequestBuilder_Method
                        .invoke(HttpRequestBuilder_Instance,
                            method,
                            JDK11.BodyPublishers_StringBody.invoke(null, data)
                        );
                }

                // final HttpResponse<String> response = HttpClient
                //      .newBuilder()
                final Object HttpClientBuilder_Instance = JDK11.HttpClient_NewBuilder.invoke(null);
                // .connectTimeout(Duration.ofSeconds(10))
                JDK11.HttpClientBuilder_ConnectTimeout
                    .invoke(HttpClientBuilder_Instance, Duration.ofSeconds(10));
                // .build()
                final Object HttpClient_Instance = JDK11.HttpClientBuilder_Build
                    .invoke(HttpClientBuilder_Instance);
                // .send(request.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                final Object HttpResponse_Instance = JDK11.HttpClient_Send
                    .invoke(HttpClient_Instance,
                        JDK11.HttpRequestBuilder_Build.invoke(HttpRequestBuilder_Instance),
                        JDK11.BodyHandlers_StringBody.invoke(null, StandardCharsets.UTF_8)
                    );

                // response.body()
                body = (String) JDK11.HttpResponse_Body.invoke(HttpResponse_Instance);
                // response.responseCode()
                code = (int) JDK11.HttpResponse_Code.invoke(HttpResponse_Instance);

            }catch(final IllegalAccessException | InvocationTargetException | ClassCastException e){
                throw new ReflectedClassException("Failed to use reflected HttpClient, please report this to the maintainers of OneMTA", e);
            }
        else{
            final HttpURLConnection conn = (HttpURLConnection) URI.create(Java9.Matcher.replaceAll(URL, blockedURI.matcher(URL), encoder)).toURL().openConnection();

            for(final Map.Entry<String, String> entry : headers.entrySet())
                conn.setRequestProperty(entry.getKey(), entry.getValue());

            conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            conn.setRequestProperty("Accept", protobuf ? "application/x-google-protobuf" : "application/json; charset=UTF-8");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            conn.setRequestMethod(method);

            if(formUrlEncoded){
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                try(final DataOutputStream OUT = new DataOutputStream(conn.getOutputStream())){
                    OUT.writeBytes(data);
                    OUT.flush();
                }
            }

            if(!protobuf)
                try(final BufferedReader IN = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
                    String buffer;
                    final StringBuilder OUT = new StringBuilder();
                    while((buffer = IN.readLine()) != null)
                        OUT.append(buffer);
                    body = OUT.toString();
                }catch(final IOException ignored){
                    body = "{}";
                }finally{
                    conn.disconnect();
                }
            else
                try(final InputStream IN = conn.getInputStream()){
                    //return GTFSRealtimeProto.FeedMessage.parseFrom(IN, registry);
                }finally{
                    conn.disconnect();
                    body = "{}";
                }

            code = conn.getResponseCode();
        }

        return new APIStruct.Response<>(URL, body, body, code);
    }

    final <T> Response<T> call(final Function<String,T> processor) throws IOException, InterruptedException{
        final Response<String> response = call();
        final String body = response.body();
        return new Response<>(response.URL(), body, processor.apply(body), response.code());
    }

    @Override
    public final String toString(){
        return "APICall{" +
               "useNetHttp=" + useNetHttp +
               ", method='" + method + '\'' +
               ", baseURL='" + baseURL + '\'' +
               ", headers=" + headers +
               ", path='" + path + '\'' +
               ", pathVars=" + pathVars +
               ", queries=" + queries +
               ", formUrlEncoded=" + formUrlEncoded +
               ", fields=" + fields +
               '}';
    }

    // replace bad URI chars

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

    // interface instantiation

    @SuppressWarnings("unchecked")
    static <C> C create(final String baseURL, final Class<C> service){
        if(!service.isInterface())
            throw new IllegalArgumentException("Service must be an interface");
        final InvocationHandler handler = new InterfaceInvocation(baseURL, service);
        return (C)
            Proxy.newProxyInstance(
                service.getClassLoader(),
                new Class<?>[]{service},
               handler
            );
    }

    private static class InterfaceInvocation implements InvocationHandler {

        private final String baseURL;
        private final Class<?> service;

        public InterfaceInvocation(final String baseURL, final Class<?> service){
            this.baseURL = baseURL;
            this.service = service;
        }

        @Override
        public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable{
            if(method.getDeclaringClass() != service)
                return method.invoke(this, args);
            try{
                return new APICall(
                    baseURL,
                    method,
                    args
                ).call(Json::parse);
            }catch(final IOException e){
                throw new UncheckedIOException(e);
            }
        }

    }

}