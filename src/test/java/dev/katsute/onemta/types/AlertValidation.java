package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Alerts;
import dev.katsute.onemta.subway.Subway;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @see TransitAlert
 * @see TransitAlertPeriod
 */
public abstract class AlertValidation {

    public static void testAlerts(final Alerts<?> alerts){
        assertNotNull(alerts);
        testAlerts(alerts.getAlerts());
        for(final TransitAlert<?,?,?,?> alert : alerts.getAlerts())
            testAlertReference(alerts, alert);
    }

    public static void testAlerts(final TransitAlert<?,?,?,?>[] alerts){
        assertNotNull(alerts);
        assumeTrue(alerts.length > 0, "No alerts found, skipping alert tests");
        for(final TransitAlert<?,?,?,?> alert : alerts){
            assertNotNull(alert.getAlertID());
            // assertNotEquals(0, alert.getActivePeriods().length); // 0 in rare elevator cases
            assertNotNull(alert.getActivePeriods());
            for(final TransitAlertPeriod period : alert.getActivePeriods()){
                assertNotNull(period.getStartEpochMillis());
                assertNotNull(period.getStart());
                assertEquals(period.getStartEpochMillis(), period.getStart().getTime());
                if(period.getEndEpochMillis() != null){
                    assertNotNull(period.getEndEpochMillis());
                    assertTrue(period.getStartEpochMillis() < period.getEndEpochMillis());
                    assertEquals(period.getEndEpochMillis(), period.getEnd().getTime());
                }
            }
            assertNotNull(alert.getRouteIDs());
            assertNotNull(alert.getStopIDs());
            assertNotEquals(0, alert.getRouteIDs().length + alert.getStopIDs().length);
            assertNotNull(alert.getHeader());
            // assertNotNull(alert.getDescription()); // missing in many cases
            assertNotNull(alert.getCreatedAt());
            assertNotNull(alert.getCreatedAtEpochMillis());
            assertEquals(alert.getCreatedAtEpochMillis(), alert.getCreatedAt().getTime());
            assertNotNull(alert.getUpdatedAt());
            assertNotNull(alert.getUpdatedAtEpochMillis());
            assertEquals(alert.getUpdatedAtEpochMillis(), alert.getUpdatedAt().getTime());
            assertNotNull(alert.getAlertType());
        }
    }

    private static void testAlertReference(final Alerts<?> reference, final TransitAlert<?,?,?,?> alert){
        final boolean route = reference instanceof TransitRoute<?,?,?>;
        final Object id = route ? ((TransitRoute<?,?,?>) reference).getRouteID() : ((TransitStop<?,?,?>) reference).getStopID();

        if(route)
            assertTrue(Arrays.asList(alert.getRouteIDs()).contains(id));
        else if(!(reference instanceof Subway.Stop)){
            assertTrue(Arrays.asList(alert.getStopIDs()).contains(id));
        }else{
            for(final Object stop : alert.getStopIDs())
                if(((Subway.Stop) reference).isSameStop(stop))
                    return;
            fail("Did not find stop '" + id + "' in " + Arrays.toString(alert.getStopIDs()));
        }
    }

}