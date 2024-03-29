package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.RouteValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestSubwayRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("subway");
        mta = TestProvider.getOneMTA();

        assert mta != null;
        route = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE);
    }

    @Test
    final void testVehicles(){
        for(final Vehicle vehicle : route.getVehicles())
            assertSame(route, vehicle.getRoute());
        TestSubwayVehicle.testVehicles(route);
        assertEquals(route.getVehicles()[0].getVehicleID(), mta.getSubwayTrain(route.getVehicles()[0].getVehicleID()).getVehicleID());
    }

    @Test
    final void testRoute(){
        assertNotNull(route.getRouteShortName());
        assertNotNull(route.getRouteDescription());
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

            assertFalse(route.isExactRoute(TestProvider.SUBWAY_ROUTE + 'x'));
            assertFalse(route.isExactRoute(TestProvider.SUBWAY_ROUTE + 'X'));
        }

        @Test
        final void testExact(){
            assertTrue(route.isExactRoute(route));
            assertTrue(route.isExactRoute(mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isExactRoute(Integer.valueOf(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isExactRoute(TestProvider.SUBWAY_ROUTE));
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
            assertTrue(route.isSameRoute(mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isSameRoute(Integer.valueOf(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE));

            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE + 'x'));
            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE + 'X'));
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