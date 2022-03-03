package dev.katsute.onemta.bus;

import dev.katsute.onemta.*;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBus {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class TestDataResource {

        @Test
        final void testRouteDRT(){
            annotateTest(() -> assertDoesNotThrow(() -> mta.getBusRoute(TestProvider.BUS_ROUTE, DataResourceType.Bus_Manhattan)));
        }

        @Test
        final void testStopDRT(){
            annotateTest(() -> assertDoesNotThrow(() -> mta.getBusStop(TestProvider.BUS_STOP, DataResourceType.Bus_Manhattan)));
        }

    }

    @Nested
    final class TestBusType {

        @Test
        final void testSelectBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("B44+").isSelectBusService()));
        }

        @Test
        final void testExpressBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("SIM1").isExpress()));
        }

        @Test
        final void testShuttleBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("F1").isShuttle()));
        }

        @Test
        final void testLimitedBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("Q44+").isLimited()));
        }

    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            annotateTest(() -> assumeTrue(mta.getBusAlerts().length > 0, "No alerts found, skipping alert tests"));

            { // missing description caused by MTA missing data
                annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                    mta.getBusAlerts(), Bus.Alert.class,
                    a -> a.getDescription() != null
                )));
            }

            for(final Bus.Alert alert : mta.getBusAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
