package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBusRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getBusRoute(TestProvider.BUS_ROUTE));
        annotateTest(() -> VehicleValidation.requireVehicles(route));
    }

    @Nested
    final class ComparatorTests {

        @Test
        final void testNotExact(){
            annotateTest(() -> assertFalse(route.isExactRoute(null)));
            annotateTest(() -> assertFalse(route.isExactRoute(999)));
            annotateTest(() -> assertFalse(route.isExactRoute("999")));

            annotateTest(() -> assertFalse(route.isExactRoute('x' + TestProvider.BUS_ROUTE)));
            annotateTest(() -> assertFalse(route.isExactRoute('X' + TestProvider.BUS_ROUTE)));
            annotateTest(() -> assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'x')));
            annotateTest(() -> assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'X')));
            annotateTest(() -> assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'c')));
            annotateTest(() -> assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + 'C')));
            annotateTest(() -> assertFalse(route.isExactRoute(TestProvider.BUS_ROUTE + '+')));
        }

        @Test
        final void testExact(){
            annotateTest(() -> assertTrue(route.isExactRoute(route)));
            annotateTest(() -> assertTrue(route.isExactRoute(mta.getBusRoute(TestProvider.BUS_ROUTE))));
            annotateTest(() -> assertTrue(route.isExactRoute(TestProvider.BUS_ROUTE)));
        }

        @Test
        final void testNotSame(){
            annotateTest(() -> assertFalse(route.isSameRoute(null)));
            annotateTest(() -> assertFalse(route.isSameRoute(999)));
            annotateTest(() -> assertFalse(route.isSameRoute("999")));
        }

        @Test
        final void testSame(){
            annotateTest(() -> assertTrue(route.isSameRoute(route)));
            annotateTest(() -> assertTrue(route.isSameRoute(mta.getBusRoute(TestProvider.BUS_ROUTE))));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE)));

            annotateTest(() -> assertTrue(route.isSameRoute('x' + TestProvider.BUS_ROUTE)));
            annotateTest(() -> assertTrue(route.isSameRoute('X' + TestProvider.BUS_ROUTE)));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'x')));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'X')));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'c')));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + 'C')));
            annotateTest(() -> assertTrue(route.isSameRoute(TestProvider.BUS_ROUTE + '+')));
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
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> v.getExpectedArrivalTime() != null &&
                            v.getExpectedArrivalTimeEpochMillis() != null &&
                            v.getExpectedDepartureTime() != null &&
                            v.getExpectedDepartureTimeEpochMillis() != null
                    )));
                }

                { // not all noProgress have a progress status for some reason
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> v.getProgressRate().equals("noProgress") &&
                            v.getProgressStatus() != null
                    )));
                }

                { // test trip refresh
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), Bus.Vehicle.class,
                        v -> {
                            final TransitTrip<?,?,?> trip = v.getTrip();
                            v.refresh();
                            return trip != v.getTrip();
                        }
                    )));
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
                annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                    route.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            BusExtensions.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                )));
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
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
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
                annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                    route.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            TripValidation.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                )));
            }

        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        final class AlertTests {

            @BeforeAll
            final void beforeAll(){
                annotateTest(() -> AlertValidation.requireAlerts(route));
            }

            @Test
            final void testTransitAlerts(){
                { // missing description caused by MTA missing data
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        route.getAlerts(), Bus.Alert.class,
                        a -> a.getDescription() != null
                    )));
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
