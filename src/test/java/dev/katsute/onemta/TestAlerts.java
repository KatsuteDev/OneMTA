package dev.katsute.onemta;

import dev.katsute.jcore.Workflow;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.TransitAlert;
import dev.katsute.onemta.types.TransitAlertPeriod;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestAlerts {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testBusAlerts(){
        annotateTest(() -> {
            final Bus.Alert[] alerts = mta.getBusAlerts();

            assumeTrue(alerts.length > 0, "No active alerts found, skipping all alerts tests");
            assertNotNull(alerts[0].getRoutes());
            assertNotNull(alerts[0].getStops());

            for(final Bus.Alert alert : alerts)
                testAlert(alert);
        });
    }

    @Test
    final void testLIRRAlerts(){
        annotateTest(() -> {
            final LIRR.Alert[] alerts = mta.getLIRRAlerts();

            assumeTrue(alerts.length > 0, "No active alerts found, skipping all alerts tests");
            assertNotNull(alerts[0].getRoutes());
            assertNotNull(alerts[0].getStops());

            for(final LIRR.Alert alert : alerts)
                testAlert(alert);
        });
    }

    @Test
    final void testMNRAlerts(){
        annotateTest(() -> {
            final MNR.Alert[] alerts = mta.getMNRAlerts();

            assumeTrue(alerts.length > 0, "No active alerts found, skipping all alerts tests");
            assertNotNull(alerts[0].getRoutes());
            assertNotNull(alerts[0].getStops());

            for(final MNR.Alert alert : alerts)
                testAlert(alert);
        });
    }

    @Test
    final void testSubwayAlerts(){
        annotateTest(() -> {
            final MNR.Alert[] alerts = mta.getMNRAlerts();

            assumeTrue(alerts.length > 0, "No active alerts found, skipping all alerts tests");
            assertNotNull(alerts[0].getRoutes());
            assertNotNull(alerts[0].getStops());

            for(final MNR.Alert alert : alerts)
                testAlert(alert);
        });
    }

    private void testAlert(final TransitAlert<?,?,?,?> alert){
        assertNotNull(alert.getID());
        assertNotNull(alert.getHeader());
        assertNotNull(alert.getDescription());
        assertNotNull(alert.getAlertType());

        assertNotNull(alert.getRouteIDs());
        assertNotNull(alert.getStopIDs());

        assertNotEquals(0, alert.getRouteIDs().length + alert.getStopIDs().length);

        assertNotNull(alert.getActivePeriods());
        assertNotEquals(0, alert.getActivePeriods().length);
        for(final TransitAlertPeriod period : alert.getActivePeriods())
            testAlertPeriod(period);
    }

    private void testAlertPeriod(final TransitAlertPeriod period){
        assertNotNull(period.getStartEpochMillis());
        assertNotNull(period.getStart());

        if(period.getEndEpochMillis() != null)
            assertNotNull(period.getEndEpochMillis());
    }

}
