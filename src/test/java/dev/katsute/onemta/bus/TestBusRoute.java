package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.RouteValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBusRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();

        route = mta.getBusRoute(TestProvider.BUS_ROUTE);
    }

    @Test
    final void testVehicles(){
        TestBusVehicle.testVehicles(route);
    }

    @Test
    final void testRoute(){
        assertNotNull(route.getRouteShortName());
        assertNotNull(route.getRouteDescription());

        assertNotNull(route.isSelectBusService());
        assertNotNull(route.isExpress());
        assertNotNull(route.isShuttle());
        assertNotNull(route.isLimited());
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

            assertFalse(route.isExactRoute('x' + TestProvider.BUS_ROUTE));
            assertFalse(route.isExactRoute('X' + TestProvider.BUS_ROUTE));
            assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'x'));
            assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'X'));
            assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'c'));
            assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'C'));
            assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + '+'));
        }

        @Test
        final void testExact(){
            assertTrue(route.isExactRoute(route));
            assertTrue(route.isExactRoute(mta.getBusRoute(TestProvider.BUS_ROUTE)));
            assertTrue(route.isExactRoute(TestProvider.BUS_ROUTE));
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
            assertTrue(route.isSameRoute(mta.getBusRoute(TestProvider.BUS_ROUTE)));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE));

            assertTrue(route.isSameRoute('x' + TestProvider.BUS_ROUTE));
            assertTrue(route.isSameRoute('X' + TestProvider.BUS_ROUTE));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'x'));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'X'));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'c'));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'C'));
            assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + '+'));
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
