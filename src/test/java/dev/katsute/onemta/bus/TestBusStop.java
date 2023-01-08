package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.StopValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBusStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();

        assert mta != null;
        stop = mta.getBusStop(TestProvider.BUS_STOP);
    }

    @Test
    final void testVehicles(){
        TestBusVehicle.testVehicles(stop);
        assertEquals(stop.getVehicles()[0].getVehicleID(), mta.getBus(stop.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Nested
    final class StopTests {

        @Test
        final void testStop(){
            StopValidation.testStop(stop);
        }

        @Test
        final void testNotExact(){
            assertFalse(stop.isExactStop(null));
            assertFalse(stop.isExactStop(999));
            assertFalse(stop.isExactStop("999"));
        }

        @Test
        final void testExact(){
            assertTrue(stop.isExactStop(stop));
            assertTrue(stop.isExactStop(mta.getBusStop(TestProvider.BUS_STOP)));
            assertTrue(stop.isExactStop(TestProvider.BUS_STOP));
            assertTrue(stop.isExactStop(String.valueOf(TestProvider.BUS_STOP)));
        }

        @Test
        final void testNotSame(){
            assertFalse(stop.isSameStop(null));
            assertFalse(stop.isSameStop(999));
            assertFalse(stop.isSameStop("999"));
        }

        @Test
        final void testSame(){
            assertTrue(stop.isSameStop(stop));
            assertTrue(stop.isSameStop(mta.getBusStop(TestProvider.BUS_STOP)));
            assertTrue(stop.isSameStop(TestProvider.BUS_STOP));
            assertTrue(stop.isSameStop(String.valueOf(TestProvider.BUS_STOP)));
        }

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(stop);
        }

    }

}
