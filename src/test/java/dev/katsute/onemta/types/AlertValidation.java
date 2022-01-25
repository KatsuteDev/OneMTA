package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Alerts;
import dev.katsute.onemta.subway.Subway;

import java.util.Arrays;

import static dev.katsute.jcore.Workflow.*;
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
        annotateTest(() -> assertNotNull(alert.getAlertID()));
        annotateTest(() -> assertNotNull(alert.getHeader()));
        annotateTest(() -> assertNotNull(alert.getDescription()));
        annotateTest(() -> assertNotNull(alert.getAlertType()));

        annotateTest(() -> assertNotNull(alert.getRouteIDs()));
        annotateTest(() -> assertNotNull(alert.getStopIDs()));
        annotateTest(() -> assertNotEquals(0, alert.getRouteIDs().length + alert.getStopIDs().length));

        annotateTest(() -> assertNotNull(alert.getActivePeriods()));
        for(final TransitAlertPeriod period : alert.getActivePeriods())
            testAlertPeriod(period);
    }

    public static void testAlertReference(final Alerts<?> reference, final TransitAlert<?,?,?,?> alert){
        final boolean stop = reference instanceof TransitStop<?,?,?>;
        final Object id = stop ? ((TransitStop<?,?,?>) reference).getStopID() : ((TransitRoute<?,?,?>) reference).getRouteID();
        annotateTest(() -> assertTrue(
            stop
            ? reference instanceof Subway.Stop
                                                                 // remove directional N/S from stop ID
                ? Arrays.asList(alert.getStopIDs()).contains(id) || Arrays.asList(alert.getStopIDs()).contains(id.toString().substring(0, id.toString().length() - 1))
                : Arrays.asList(alert.getStopIDs()).contains(id)
            : Arrays.asList(alert.getRouteIDs()).contains(id))
        );
    }

    //

    private static void testAlertPeriod(final TransitAlertPeriod period){
        annotateTest(() -> assertNotNull(period.getStartEpochMillis()));
        annotateTest(() -> assertNotNull(period.getStart()));

        if(period.getEndEpochMillis() != null)
            annotateTest(() -> assertNotNull(period.getEndEpochMillis()));
    }

}
