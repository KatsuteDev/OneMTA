package dev.katsute.onemta;

import org.junit.jupiter.params.provider.Arguments;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public abstract class TestProvider {

    private static final File test_resources = new File("src/test/java/resources");

    private static final File bus    = new File(test_resources, "token_bus.txt");
    private static final File subway = new File(test_resources, "token_subway.txt");

    private static final boolean hasBus    = bus.exists();
    private static final boolean hasSubway = subway.exists();

    private static final Map<DataResourceType,String> resources = new HashMap<DataResourceType,String>(){{
        put(DataResourceType.Subway             , "http://web.mta.info/developers/data/nyct/subway/google_transit.zip");
        put(DataResourceType.Bus_Bronx          , "http://web.mta.info/developers/data/nyct/bus/google_transit_bronx.zip");
        put(DataResourceType.Bus_Brooklyn       , "http://web.mta.info/developers/data/nyct/bus/google_transit_brooklyn.zip");
        put(DataResourceType.Bus_Manhattan      , "http://web.mta.info/developers/data/nyct/bus/google_transit_manhattan.zip");
        put(DataResourceType.Bus_Queens         , "http://web.mta.info/developers/data/nyct/bus/google_transit_queens.zip");
        put(DataResourceType.Bus_StatenIsland   , "http://web.mta.info/developers/data/nyct/bus/google_transit_staten_island.zip");
        put(DataResourceType.LongIslandRailroad , "http://web.mta.info/developers/data/lirr/google_transit.zip");
        put(DataResourceType.MetroNorthRailroad , "http://web.mta.info/developers/data/mnr/google_transit.zip");
        put(DataResourceType.Bus_Company        , "http://web.mta.info/developers/data/busco/google_transit.zip");
    }};

    public static OneMTA getOneMTA(){
        try{
            if(!hasBus && !hasSubway)
                annotateTest(() -> assumeTrue(false, "No token defined, skipping tests"));

            for(final Map.Entry<DataResourceType,String> entry : resources.entrySet())
                try(final BufferedInputStream IN = new BufferedInputStream(new URL(entry.getValue()).openStream())){
                    final File file = new File(test_resources, "resource_" + entry.getKey().name().toLowerCase() + ".zip");
                    if(!file.exists())
                        try(final FileOutputStream OUT = new FileOutputStream(file)){
                            byte[] buffer = new byte[1024];
                            int bytesReads;
                            while((bytesReads = IN.read(buffer, 0, 1024)) != -1)
                                OUT.write(buffer, 0, bytesReads);
                        }
                }

            final List<DataResource> resources = new ArrayList<>();
            for(final DataResourceType type : TestProvider.resources.keySet())
                resources.add(DataResource.create(type, new File(test_resources, "resource_" + type.name().toLowerCase() + ".zip")));

            return OneMTA.create(strip(readFile(bus)), strip(readFile(subway)), resources.toArray(new DataResource[0]));
        }catch(final IOException e){
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            annotateTest(() -> fail(sw.toString()));
            return null;
        }
    }

    // java 9

    public static String readFile(final File file) throws IOException{
        final StringBuilder OUT = new StringBuilder();
        for(final String s : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8))
            OUT.append(s);
        return OUT.toString();
    }

    // ^(\s+)|(\s+)$
    private static final Pattern dangling = Pattern.compile("^(\\s+)|(\\s+)$");

    static String strip(final String s){
        return dangling.matcher(s).replaceAll("");
    }

    //

    public static final class MethodStream<T> {

        private final List<Arguments> args = new ArrayList<>();

        public final MethodStream<T> add(final Function<T,Object> function){
            args.add(Arguments.of(function));
            return this;
        }

        public final MethodStream<T> add(final String method, final Function<T,Object> function){
            args.add(Arguments.of(method, function));
            return this;
        }

        public final Stream<Arguments> stream(){
            return args.stream();
        }

    }

    public static final class ObjectStream {

        private final List<Arguments> args = new ArrayList<>();

        public final ObjectStream add(final Object... object){
            args.add(Arguments.of(object));
            return this;
        }

        public final Stream<Arguments> stream(){
            return args.stream();
        }

    }

}