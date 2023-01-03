package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Alerts;
import dev.katsute.onemta.subway.Subway;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @see TransitAlert
 * @see TransitAlertPeriod
 */
public abstract class AlertValidation {

    public static void requireAlerts(final Alerts<?> alerts){
        assumeTrue(alerts.getAlerts().length > 0, "No alerts found, skipping alert tests");
    }

    //

    public static void testAlert(final TransitAlert<?,?,?,?> alert){
        assertNotNull(alert.getAlertID());
        assertNotNull(alert.getHeader());
        //assertNotNull(alert.getDescription());
        assertNotNull(alert.getAlertType());
        assertNotNull(alert.getEffect());

        assertNotNull(alert.getRouteIDs());
        assertNotNull(alert.getStopIDs());
        assertNotEquals(0, alert.getRouteIDs().length + alert.getStopIDs().length);

        assertNotNull(alert.getActivePeriods());
        for(final TransitAlertPeriod period : alert.getActivePeriods())
            testAlertPeriod(period);
    }

    private static final Pattern direction = Pattern.compile("N|S$", Pattern.CASE_INSENSITIVE);

    private static String stripDirection(final String stop){
        return direction.matcher(stop).replaceAll("");
    }


    public static void testAlertReference(final Alerts<?> reference, final TransitAlert<?,?,?,?> alert){
        final boolean stop = reference instanceof TransitStop<?,?,?>;
        final Object id = stop ? ((TransitStop<?,?,?>) reference).getStopID() : ((TransitRoute<?,?,?>) reference).getRouteID();

        if(!stop)
            assertTrue(Arrays.asList(alert.getRouteIDs()).contains(id), "Failed to find route " + id + " in " + Arrays.toString(alert.getRouteIDs()));
        else if(!(reference instanceof Subway.Stop))
            assertTrue(Arrays.asList(alert.getStopIDs()).contains(id), "Failed to find stop " + id + " in " + Arrays.toString(alert.getStopIDs()));
        else{
            for(final Object stopID : alert.getStopIDs())
                if(stripDirection(stopID.toString()).equalsIgnoreCase(stripDirection(id.toString())))
                    return;
            fail("Failed to find stop " + id + " in " + Arrays.toString(alert.getStopIDs()));
        }
    }

    //

    private static void testAlertPeriod(final TransitAlertPeriod period){
        assertNotNull(period.getStartEpochMillis());
        assertNotNull(period.getStart());

        if(period.getEndEpochMillis() != null)
            assertNotNull(period.getEndEpochMillis());
    }

}
