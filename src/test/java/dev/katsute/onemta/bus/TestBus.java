package dev.katsute.onemta.bus;

import dev.katsute.onemta.*;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
            assertDoesNotThrow(() -> mta.getBusRoute(TestProvider.BUS_ROUTE, DataResourceType.Bus_Manhattan));
        }

        @Test
        final void testStopDRT(){
            assertDoesNotThrow(() -> mta.getBusStop(TestProvider.BUS_STOP, DataResourceType.Bus_Manhattan));
        }

    }

    @Nested
    final class TestBusType {

        @Test
        final void testSelectBus(){
            assertTrue(mta.getBusRoute("B44+").isSelectBusService());
        }

        @Test
        final void testExpressBus(){
            assertTrue(mta.getBusRoute("SIM1").isExpress());
        }

        @Test
        final void testShuttleBus(){
            assertTrue(mta.getBusRoute("F1").isShuttle());
        }

        @Test
        final void testLimitedBus(){
            assertTrue(mta.getBusRoute("Q44+").isLimited());
        }

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(mta.getBusAlerts());
        }

    }

}
