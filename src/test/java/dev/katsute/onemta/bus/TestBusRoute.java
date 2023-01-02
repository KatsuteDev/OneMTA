package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.RouteValidation;
import dev.katsute.onemta.types.TripValidation;
import dev.katsute.onemta.types.VehicleValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
        VehicleValidation.requireVehicles(route);
    }

    @Nested
    final class ComparatorTests {

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
    final class ExtensionTests {

        @Test
        final void testRoute(){
            BusExtensions.testRoute(route);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // not all bus vehicles have this for some reason
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> v.getExpectedArrivalTime() != null &&
                            v.getExpectedArrivalTimeEpochMillis() != null &&
                            v.getExpectedDepartureTime() != null &&
                            v.getExpectedDepartureTimeEpochMillis() != null
                    ));
                }

                { // not all noProgress have a progress status for some reason
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> v.getProgressRate().equals("noProgress") &&
                            v.getProgressStatus() != null
                    ));
                }

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
            final void testOrigin(){
                BusExtensions.testOriginStop(route.getVehicles()[0]);
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

        @Test
        final void testTransitRoute(){
            RouteValidation.testRoute(route);
        }

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

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        final class AlertTests {

            @BeforeAll
            final void beforeAll(){
                AlertValidation.requireAlerts(route);
            }

            @Test
            final void testTransitAlerts(){
                { // missing description caused by MTA missing data
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getAlerts(), Bus.Alert.class,
                        a -> a.getDescription() != null
                    ));
                }

                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlertReference(route, alert);
            }

        }

    }

}
