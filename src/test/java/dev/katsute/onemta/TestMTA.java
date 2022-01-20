package dev.katsute.onemta;

import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMTA {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA(null);
    }

    @Nested
    final class BusTests {

        @Test
        final void testRoute(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getBusRoute("NULL")));
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getBusRoute(null)));
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getBusRoute(null, null)));
        }

        @Test
        final void testNullVehicle(){
            annotateTest(() -> assertNull(mta.getBus(0)));
        }

    }

    @Nested
    final class SubwayTests {

        @Test
        final void testRoute(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayRoute("NULL")));
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayRoute(null)));
        }

        @Test
        final void testStop(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayStop(null)));
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayStop(Integer.parseInt(TestProvider.SUBWAY_STOP), null)));
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayStop(null, null)));
        }

        @Test
        final void testNullVehicle(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getSubwayTrain(null)));
            annotateTest(() -> assertNull(mta.getSubwayTrain("01 NULL")));
        }

    }

    @Nested
    final class LIRRTests {

        @Test
        final void testRoute(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getLIRRRoute(-1)));
        }

        @Test
        final void testStop(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getLIRRStop(null)));
        }

        @Test
        final void testNullVehicle(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getLIRRTrain(null)));
            annotateTest(() -> assertNull(mta.getLIRRTrain("NULL")));
        }

    }

    @Nested
    final class MNRTests {

        @Test
        final void testRoute(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getMNRRoute(-1)));
        }

        @Test
        final void testStop(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getMNRStop(null)));
        }

        @Test
        final void testNullVehicle(){
            annotateTest(() -> assertThrows(NullPointerException.class, () -> mta.getMNRStop(null)));
            annotateTest(() -> assertNull(mta.getMNRTrain("NULL")));
        }

    }

}
