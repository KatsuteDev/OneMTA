package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.RouteValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNRRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();

        assert mta != null;
        route = mta.getMNRRoute(TestProvider.MNR_ROUTE);
    }

    @Test
    final void testVehicles(){
        TestMNRVehicle.testVehicles(route);
        for(final Vehicle vehicle : route.getVehicles())
            assertSame(route, vehicle.getRoute());
        assertEquals(route.getVehicles()[0].getVehicleID(), mta.getMNRTrain(route.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Nested
    final class RouteTests {

        @Test
        final void testRoute(){
            RouteValidation.testRoute(route);
        }

        @Test
        final void testNotExact(){
            assertFalse(route.isExactRoute(null));
            assertFalse(route.isExactRoute(999));
            assertFalse(route.isExactRoute("999"));
        }

        @Test
        final void testExact(){
            assertTrue(route.isExactRoute(route));
            assertTrue(route.isExactRoute(mta.getMNRRoute(TestProvider.MNR_ROUTE)));
            assertTrue(route.isExactRoute(TestProvider.MNR_ROUTE));
            assertTrue(route.isExactRoute(String.valueOf(TestProvider.MNR_ROUTE)));
        }

        @Test
        final void testNotSame(){
            assertFalse(route.isSameRoute(null));
            assertFalse(route.isSameRoute(999));
            assertFalse(route.isSameRoute("999"));
        }

        @Test
        final void testSame(){
            assertTrue(route.isSameRoute(route));
            assertTrue(route.isSameRoute(mta.getMNRRoute(TestProvider.MNR_ROUTE)));
            assertTrue(route.isSameRoute(TestProvider.MNR_ROUTE));
            assertTrue(route.isSameRoute(String.valueOf(TestProvider.MNR_ROUTE)));
        }

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(route);
        }

    }

}
