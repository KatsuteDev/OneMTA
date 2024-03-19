package dev.katsute.onemta;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SuppressWarnings({"SpellCheckingInspection", "Convert2Diamond"})
public abstract class TestProvider {

    public static final String BUS_ROUTE = "M1";
    public static final int BUS_STOP = 400561;

    public static final int LIRR_ROUTE = 9;
    public static final int LIRR_STOP = 56;
    public static final String LIRR_STOP_CODE = "FLS";

    public static final int MNR_ROUTE = 2;
    public static final int MNR_STOP = 59;
    public static final String MNR_STOP_CODE = "1WN";

    public static final String SUBWAY_ROUTE = "7";
    public static final String SUBWAY_STOP = "631";

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
            long now = System.currentTimeMillis();
            final int delay = TEST_DELAY * 1000;
            final long lastTest = TEST_LOCK.exists() ? Long.parseLong(readFile(TEST_LOCK)) : -1;
            final long allowedPass = lastTest + delay;

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

    private static final File bus = new File(test_resources, "bus.txt");

    private static final boolean hasBus = bus.exists();

    private static final List<DataResourceType> resources = Arrays.asList(
        DataResourceType.Subway,
        DataResourceType.Bus_Bronx,
        DataResourceType.Bus_Brooklyn,
        DataResourceType.Bus_Manhattan,
        DataResourceType.Bus_Queens,
        DataResourceType.Bus_StatenIsland,
        DataResourceType.LongIslandRailroad,
        DataResourceType.MetroNorthRailroad,
        DataResourceType.Bus_Company
    );

    public static MTA mta;

    public static MTA getOneMTA(){
        try{
            if(!hasBus)
                assumeTrue(false, "No token defined, skipping tests");

            acquireTestLock();

            final List<DataResource> resources = new ArrayList<>();
            for(final DataResourceType type : TestProvider.resources)
                resources.add(DataResource.create(type, new File(test_resources, "resource_" + type.name().toLowerCase() + ".zip")));

            return mta = MTA.create(strip(readFile(bus)), resources.toArray(new DataResource[0]));
        }catch(final IOException e){
            fail(e);
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

}