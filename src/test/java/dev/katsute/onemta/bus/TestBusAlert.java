package dev.katsute.onemta.bus;

import dev.katsute.jcore.Workflow;
import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TransitAlertPeriod;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.bus.Bus.*;

final class TestBusAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testAlerts(){
        Workflow.annotateTest(() -> {
            final Alert[] alerts = mta.getBusAlerts();

            Assumptions.assumeTrue(alerts.length > 0, "No active alerts found, skipping all alerts tests");
            Assertions.assertNotNull(alerts[0].getRoutes());
            Assertions.assertNotNull(alerts[0].getStops());

            for(final Alert alert : alerts)
                testAlert(alert);
        });
    }

    private void testAlert(final Alert alert){
        Assertions.assertNotNull(alert.getID());
        Assertions.assertNotNull(alert.getHeader());
        Assertions.assertNotNull(alert.getDescription());
        Assertions.assertNotNull(alert.getAlertType());

        Assertions.assertNotNull(alert.getRouteIDs());
        Assertions.assertNotNull(alert.getStopIDs());

        Assertions.assertNotEquals(0, alert.getRouteIDs().length + alert.getStopIDs().length);

        Assertions.assertNotNull(alert.getActivePeriods());
        Assertions.assertNotEquals(0, alert.getActivePeriods().length);
        for(final TransitAlertPeriod period : alert.getActivePeriods())
            testAlertPeriod(period);
    }

    private void testAlertPeriod(final TransitAlertPeriod period){
        Assertions.assertNotNull(period.getStartEpochMillis());
        Assertions.assertNotNull(period.getStart());

        if(period.getEndEpochMillis() != null)
            Assertions.assertNotNull(period.getEndEpochMillis());
    }

}
