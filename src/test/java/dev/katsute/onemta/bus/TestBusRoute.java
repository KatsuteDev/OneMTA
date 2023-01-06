package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
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

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // test trip refresh
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> {
                            final Bus.Vehicle temp = mta.getBus(v.getVehicleID());
                            final Bus.Trip trip = temp.getTrip();
                            temp.refresh();
                            return trip != temp.getTrip();
                        }
                    ));
                }

                for(final Vehicle vehicle : route.getVehicles())
                    BusExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                BusExtensions.testVehicleNumber(mta, route.getVehicles()[0]);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                assertTrue(TestProvider.atleastOneTrue(
                    route.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            BusExtensions.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                ));
            }

        }

    }

    @Nested
    final class InheritedTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testTransitVehicles(){
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testVehicleRouteReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicleRouteReference(route, vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                for(final Vehicle vehicle : route.getVehicles()){
                    assertNotNull(vehicle.getTrip());
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testVehicleTripRouteReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripRouteReference(vehicle);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                assertTrue(TestProvider.atleastOneTrue(
                    route.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            TripValidation.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                ));
            }

        }

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
