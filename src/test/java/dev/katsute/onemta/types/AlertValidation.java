package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Alerts;

import java.util.Arrays;
import java.util.Random;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @see TransitAlert
 * @see TransitAlertPeriod
 */
public abstract class AlertValidation {

    public static void testAlerts(final TransitAlert<?,?,?,?>[] alerts){
        annotateTest(() -> assumeTrue(alerts.length > 0, "No active alerts found, skipping alert tests"));

        final int rand = new Random().nextInt(alerts.length);

        annotateTest(() -> assertNotNull(alerts[rand].getRoutes()));
        annotateTest(() -> assertNotNull(alerts[rand].getStops()));

        for(final TransitAlert<?,?,?,?> alert : alerts)
            testAlert(alert);
    }

    public static void testAlerts(final Alerts<?> reference){
        testAlerts(reference.getAlerts());

        final boolean stop = reference instanceof TransitStop<?,?,?>;
        final Object id = stop ? ((TransitStop<?,?,?>) reference).getStopID() : ((TransitRoute<?,?,?>) reference).getRouteID();
        for(final TransitAlert<?,?,?,?> alert : reference.getAlerts())
            annotateTest(() -> assertTrue(stop ? Arrays.asList(alert.getStopIDs()).contains(id) : Arrays.asList(alert.getRouteIDs()).contains(id)));
    }

    private static void testAlert(final TransitAlert<?,?,?,?> alert){
        annotateTest(() -> assertNotNull(alert.getID()));
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

    //

    private static void testAlertPeriod(final TransitAlertPeriod period){
        annotateTest(() -> assertNotNull(period.getStartEpochMillis()));
        annotateTest(() -> assertNotNull(period.getStart()));

        if(period.getEndEpochMillis() != null)
            annotateTest(() -> assertNotNull(period.getEndEpochMillis()));
    }

}
