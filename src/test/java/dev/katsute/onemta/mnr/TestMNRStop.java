package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.StopValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();

        assert mta != null;
        stop = mta.getMNRStop(TestProvider.MNR_STOP);
    }

    @Test
    final void testVehicles(){
        TestMNRVehicle.testVehicles(stop);
        assertEquals(stop.getVehicles()[0].getVehicleID(), mta.getMNRTrain(stop.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Test
    final void testStop(){
        assertNotNull(stop.getStopCode());
        assertNotNull(stop.hasWheelchairBoarding());
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

            assertTrue(stop.isExactStop(TestProvider.MNR_STOP_CODE));
            assertTrue(stop.isExactStop(TestProvider.MNR_STOP_CODE.toLowerCase()));
        }

        @Test
        final void testExact(){
            assertTrue(stop.isExactStop(stop));
            assertTrue(stop.isExactStop(mta.getMNRStop(TestProvider.MNR_STOP)));
            assertTrue(stop.isExactStop(TestProvider.MNR_STOP));
            assertTrue(stop.isExactStop(String.valueOf(TestProvider.MNR_STOP)));
        }

        @Test
        final void testNotSame(){
            assertFalse(stop.isSameStop(null));
            assertFalse(stop.isSameStop(999));
            assertFalse(stop.isSameStop("999"));

            assertTrue(stop.isSameStop(TestProvider.MNR_STOP_CODE));
            assertTrue(stop.isSameStop(TestProvider.MNR_STOP_CODE.toLowerCase()));
        }

        @Test
        final void testSame(){
            assertTrue(stop.isSameStop(stop));
            assertTrue(stop.isSameStop(mta.getMNRStop(TestProvider.MNR_STOP)));
            assertTrue(stop.isSameStop(TestProvider.MNR_STOP));
            assertTrue(stop.isSameStop(String.valueOf(TestProvider.MNR_STOP)));
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