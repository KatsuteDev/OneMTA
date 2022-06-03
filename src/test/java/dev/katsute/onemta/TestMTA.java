package dev.katsute.onemta;

import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

final class TestMTA {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("MTA");
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class BusTests {

        @Test
        final void testRoute(){
            assertThrows(NullPointerException.class, () -> mta.getBusRoute("NULL"));
            assertThrows(NullPointerException.class, () -> mta.getBusRoute(null));
            assertThrows(NullPointerException.class, () -> mta.getBusRoute(null, null));
        }

        @Test
        final void testNullVehicle(){
            assertNull(mta.getBus(0));
        }

    }

    @Nested
    final class SubwayTests {

        @Test
        final void testRoute(){
            assertThrows(NullPointerException.class, () -> mta.getSubwayRoute("NULL"));
            assertThrows(NullPointerException.class, () -> mta.getSubwayRoute(null));
        }

        @Test
        final void testStop(){
            assertThrows(NullPointerException.class, () -> mta.getSubwayStop(null));
            assertThrows(NullPointerException.class, () -> mta.getSubwayStop(Integer.parseInt(TestProvider.SUBWAY_STOP), null));
            assertThrows(NullPointerException.class, () -> mta.getSubwayStop(null, null));
        }

        @Test
        final void testNullVehicle(){
            assertThrows(NullPointerException.class, () -> mta.getSubwayTrain(null));
            assertNull(mta.getSubwayTrain("01 NULL"));
        }

    }

    @Nested
    final class LIRRTests {

        @Test
        final void testRoute(){
            assertThrows(NullPointerException.class, () -> mta.getLIRRRoute(-1));
        }

        @Test
        final void testStop(){
            assertThrows(NullPointerException.class, () -> mta.getLIRRStop(null));
        }

        @Test
        final void testNullVehicle(){
            assertThrows(NullPointerException.class, () -> mta.getLIRRTrain(null));
            assertNull(mta.getLIRRTrain("NULL"));
        }

    }

    @Nested
    final class MNRTests {

        @Test
        final void testRoute(){
            assertThrows(NullPointerException.class, () -> mta.getMNRRoute(-1));
        }

        @Test
        final void testStop(){
            assertThrows(NullPointerException.class, () -> mta.getMNRStop(null));
        }

        @Test
        final void testNullVehicle(){
            assertThrows(NullPointerException.class, () -> mta.getMNRStop(null));
            assertNull(mta.getMNRTrain("NULL"));
        }

    }

    @Test @Disabled
    final void testToString(){
        final Bus.Route br = mta.getBusRoute(TestProvider.BUS_ROUTE);
        System.out.println(br);
        System.out.println(Arrays.toString(br.getVehicles()));
        System.out.println(Arrays.toString(br.getAlerts()));
        final Bus.Stop bs = mta.getBusStop(TestProvider.BUS_STOP);
        System.out.println(bs);
        System.out.println(Arrays.toString(bs.getVehicles()));
        System.out.println(Arrays.toString(bs.getAlerts()));
        final Subway.Route sr = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE);
        System.out.println(sr);
        System.out.println(Arrays.toString(sr.getVehicles()));
        System.out.println(Arrays.toString(sr.getAlerts()));
        final Subway.Stop st = mta.getSubwayStop(TestProvider.SUBWAY_STOP);
        System.out.println(st);
        System.out.println(Arrays.toString(st.getVehicles()));
        System.out.println(Arrays.toString(st.getAlerts()));
        final LIRR.Route lr = mta.getLIRRRoute(TestProvider.LIRR_ROUTE);
        System.out.println(lr);
        System.out.println(Arrays.toString(lr.getVehicles()));
        System.out.println(Arrays.toString(lr.getAlerts()));
        final LIRR.Stop ls = mta.getLIRRStop(TestProvider.LIRR_STOP_CODE);
        System.out.println(ls);
        System.out.println(Arrays.toString(ls.getVehicles()));
        System.out.println(Arrays.toString(ls.getAlerts()));
        final MNR.Route mr = mta.getMNRRoute(TestProvider.MNR_ROUTE);
        System.out.println(mr);
        System.out.println(Arrays.toString(mr.getVehicles()));
        System.out.println(Arrays.toString(mr.getAlerts()));
        final MNR.Stop ms = mta.getMNRStop(TestProvider.MNR_STOP_CODE);
        System.out.println(ms);
        System.out.println(Arrays.toString(ms.getVehicles()));
        System.out.println(Arrays.toString(ms.getAlerts()));
    }

}
