package dev.katsute.onemta;

import org.junit.jupiter.params.provider.Arguments;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SuppressWarnings("SpellCheckingInspection")
public abstract class TestProvider {

    public static final String BUS_ROUTE = "M1";
    public static final int BUS_STOP     = 400561;

    public static final int LIRR_ROUTE        = 9;
    public static final int LIRR_STOP         = 56;
    public static final String LIRR_STOP_CODE = "FLS";

    public static final int MNR_ROUTE        = 2;
    public static final int MNR_STOP         = 59;
    public static final String MNR_STOP_CODE = "1WN";

    public static final String SUBWAY_ROUTE = "7";
    public static final String SUBWAY_STOP  = "631";

    //

    private static final File test_resources = new File("src/test/java/resources");

    //

    private static final File TEST_GROUP = new File(test_resources, "TEST_GROUP");

    public static void testGroup(final String testGroup){
        try{
            if("true".equalsIgnoreCase(System.getenv("CI")) && testGroup != null && TEST_GROUP.exists()){
                final String expected = readFile(TEST_GROUP);
                if(!expected.equalsIgnoreCase(testGroup))
                    assumeTrue(false, "Skipped testing group " + testGroup + ", only testing " + expected);
            }
        }catch(final IOException e){
            fail(e);
        }
    }

    //

    private static final int TEST_DELAY = 60;
    private static final File TEST_LOCK = new File(test_resources, "TEST_LOCK");

    public static void acquireTestLock(){
        try{
            System.out.println("[↻] Checking rate limit...");
            long now                = System.currentTimeMillis();
            final int delay         = TEST_DELAY * 1000;
            final long lastTest     = TEST_LOCK.exists() ? Long.parseLong(readFile(TEST_LOCK)) : -1;
            final long allowedPass  = lastTest + delay;

            if(lastTest != - 1 && now < allowedPass){
                System.out.println("[⏸] Rate limit in place, waiting " + ((allowedPass - now) / 1000) + " seconds");
                Thread.sleep(allowedPass - now);
            }

            Files.write(TEST_LOCK.toPath(), String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
            System.out.println("[✔] Test lock acquired");
        }catch(IOException | InterruptedException e){
            fail(e);
        }
    }

    //

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

    public static MTA mta;

    public static MTA getOneMTA(){
        try{
            if(!hasBus && !hasSubway)
                assumeTrue(false, "No token defined, skipping tests");

            acquireTestLock();

            for(final Map.Entry<DataResourceType,String> entry : resources.entrySet())
                try(final BufferedInputStream IN = new BufferedInputStream(new URL(entry.getValue()).openStream())){
                    final File file = new File(test_resources, "resource_" + entry.getKey().name().toLowerCase() + ".zip");
                    System.out.println("[↻] Checking for data resource " + file.getName());
                    if(!file.exists()){
                        System.out.println("[⚠] " + file.getName() + " not found, downloading from MTA...");
                        try(final FileOutputStream OUT = new FileOutputStream(file)){
                            byte[] buffer = new byte[1024];
                            int bytesReads;
                            while((bytesReads = IN.read(buffer, 0, 1024)) != -1)
                                OUT.write(buffer, 0, bytesReads);
                        }
                    }
                    System.out.println("[✔] Added " + file.getName() + " as " + entry.getKey().name());
                }

            final List<DataResource> resources = new ArrayList<>();
            for(final DataResourceType type : TestProvider.resources.keySet())
                resources.add(DataResource.create(type, new File(test_resources, "resource_" + type.name().toLowerCase() + ".zip")));

            return mta = MTA.create(strip(readFile(bus)), strip(readFile(subway)), resources.toArray(new DataResource[0]));
        }catch(final IOException e){
            fail(e);
            return null;
        }
    }

    public static <T> boolean atleastOneTrue(final T[] array, @SuppressWarnings("unused") final Class<T> T, final Predicate<T> predicate){
        boolean passes = false;
        for(final T t : array)
            if(predicate.test(t))
                passes = true;
        return passes;
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
