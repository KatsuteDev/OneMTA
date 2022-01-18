package dev.katsute.onemta.types;

import java.util.Random;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public final class TestAlerts {

    public static void testAlerts(final TransitAlert<?,?,?,?>[] alerts){
        annotateTest(() -> assumeTrue(alerts.length > 0, "No active alerts found, skipping alert tests"));

        final int rand = new Random().nextInt(alerts.length);

        annotateTest(() -> assertNotNull(alerts[rand].getRoutes()));
        annotateTest(() -> assertNotNull(alerts[rand].getStops()));

        for(final TransitAlert<?, ?, ?, ?> alert : alerts)
            testAlert(alert);
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

    private static void testAlertPeriod(final TransitAlertPeriod period){
        annotateTest(() -> assertNotNull(period.getStartEpochMillis()));
        annotateTest(() -> assertNotNull(period.getStart()));

        if(period.getEndEpochMillis() != null)
            annotateTest(() -> assertNotNull(period.getEndEpochMillis()));
    }

}
