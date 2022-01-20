package dev.katsute.onemta.bus;

import dev.katsute.onemta.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBus {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("bus");
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

    //

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

}
