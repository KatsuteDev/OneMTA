package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.StopValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestSubwayStop {

    private static MTA mta;

    private static Stop stop;
    private static Stop stopN;
    private static Stop stopS;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("subway");
        mta = TestProvider.getOneMTA();

        assert mta != null;
        stop = mta.getSubwayStop(TestProvider.SUBWAY_STOP);
        stopN = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "N");
        stopS = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "S");
    }

    @Test
    final void testVehicles(){
        TestSubwayVehicle.testVehicles(stop);
        assertEquals(stop.getVehicles()[0].getVehicleID(), mta.getSubwayTrain(stop.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Test
    final void testVehiclesN(){
        TestSubwayVehicle.testVehicles(stopN);
        assertEquals(stopN.getVehicles()[0].getVehicleID(), mta.getSubwayTrain(stopN.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Test
    final void testVehiclesS(){
        TestSubwayVehicle.testVehicles(stopS);
        assertEquals(stopS.getVehicles()[0].getVehicleID(), mta.getSubwayTrain(stopS.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Test
    final void testStop(){
        assertNotNull(stop.getTransfers());
        assertNotEquals(0, stop.getTransfers().length);
    }

    @Nested
    final class StopTests {

        @Test
        final void testStop(){
            StopValidation.testStop(stop);
            assertNull(stop.getDirection());
        }

        @Test
        final void testStopN(){
            StopValidation.testStop(stopN);
            assertEquals(SubwayDirection.NORTH, stopN.getDirection());
        }

        @Test
        final void testStopS(){
            StopValidation.testStop(stopS);
            assertEquals(SubwayDirection.SOUTH, stopS.getDirection());
        }

        @Test
        final void testNotExact(){
            assertFalse(stop.isExactStop(null));
            assertFalse(stop.isExactStop(999));
            assertFalse(stop.isExactStop("999"));

            assertFalse(stop.isExactStop(TestProvider.SUBWAY_STOP + 'n'));
            assertFalse(stop.isExactStop(TestProvider.SUBWAY_STOP + 'N'));
            assertFalse(stop.isExactStop(TestProvider.SUBWAY_STOP + 's'));
            assertFalse(stop.isExactStop(TestProvider.SUBWAY_STOP + 'S'));

            assertFalse(stop.isExactStop(stopN));
            assertFalse(stop.isExactStop(stopS));
            assertFalse(stopS.isExactStop(stopN));
            assertFalse(stopN.isExactStop(stopS));
        }

        @Test
        final void testExact(){
            assertTrue(stop.isExactStop(stop));
            assertTrue(stop.isExactStop(mta.getSubwayStop(TestProvider.SUBWAY_STOP)));
            assertTrue(stop.isExactStop(Integer.valueOf(TestProvider.SUBWAY_STOP)));
            assertTrue(stop.isExactStop(TestProvider.SUBWAY_STOP));
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
            assertTrue(stop.isSameStop(mta.getSubwayStop(TestProvider.SUBWAY_STOP)));
            assertTrue(stop.isSameStop(Integer.valueOf(TestProvider.SUBWAY_STOP)));
            assertTrue(stop.isSameStop(TestProvider.SUBWAY_STOP));

            assertTrue(stop.isSameStop(TestProvider.SUBWAY_STOP + 'n'));
            assertTrue(stop.isSameStop(TestProvider.SUBWAY_STOP + 'N'));
            assertTrue(stop.isSameStop(TestProvider.SUBWAY_STOP + 's'));
            assertTrue(stop.isSameStop(TestProvider.SUBWAY_STOP + 'S'));

            assertTrue(stop.isSameStop(stopN));
            assertTrue(stop.isSameStop(stopS));
            assertTrue(stopN.isSameStop(stopN));
            assertTrue(stopS.isSameStop(stopN));
            assertTrue(stopN.isSameStop(stopS));
            assertTrue(stopS.isSameStop(stopS));
        }

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(stop);
        }

        @Test
        final void testTransitAlertN(){
            AlertValidation.testAlerts(stopN);
        }

        @Test
        final void testTransitAlertS(){
            AlertValidation.testAlerts(stopS);
        }

    }

}