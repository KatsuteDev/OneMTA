package dev.katsute.onemta.bus;

import dev.katsute.jcore.Workflow;
import dev.katsute.onemta.*;
import org.junit.jupiter.api.*;

final class TestBus {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testRouteDRT(){
        Workflow.annotateTest(() -> Assertions.assertDoesNotThrow(() -> mta.getBusRoute(TestProvider.BUS_ROUTE, DataResourceType.Bus_Manhattan)));
    }

    @Test
    final void testStopDRT(){
        Workflow.annotateTest(() -> Assertions.assertDoesNotThrow(() -> mta.getBusStop(TestProvider.BUS_STOP, DataResourceType.Bus_Manhattan)));
    }

}
