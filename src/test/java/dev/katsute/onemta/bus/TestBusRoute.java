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
                    boolean tested = false;
                    for(final Vehicle vehicle : route.getVehicles()){
                        if(vehicle.getExpectedArrivalTime() != null &&
                           vehicle.getExpectedArrivalTimeEpochMillis() != null &&
                           vehicle.getExpectedDepartureTime() != null &&
                           vehicle.getExpectedDepartureTimeEpochMillis() != null
                        ){
                            tested = true;
                            break;
                        }
                    }

                    final boolean finalPasses = tested;
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests, there probably wasn't enough vehicles to conclude test (tested " + route.getVehicles().length + " vehicles)"));
                }

                { // not all noProgress have a progress status for some reason
                    boolean tested = false;
                    for(final Vehicle vehicle : route.getVehicles()){
                        if(vehicle.getProgressRate().equals("noProgress") && vehicle.getProgressStatus() != null){
                            tested = true;
                            break;
                        }
                    }
                    final boolean finalPasses = tested;
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass progress tests, there probably wasn't enough vehicles to conclude test (tested " + route.getVehicles().length + " vehicles)"));
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
                boolean tested = false;
                for(final Vehicle vehicle : route.getVehicles())
                    if(vehicle.getTrip() != null && vehicle.getTrip().getTripStops().length > 0){
                        BusExtensions.testTripStops(vehicle.getTrip().getTripStops());
                        tested = true;
                    }
                final boolean finalTested = tested;
                annotateTest(() -> assertTrue(finalTested, "Failed to pass trip stop tests, there probably wasn't enough vehicles to conclude test (tested " + route.getVehicles().length + " vehicles)"));
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
                boolean tested = false;
                for(final Vehicle vehicle : route.getVehicles())
                    if(vehicle.getTrip() != null && vehicle.getTrip().getTripStops().length > 0){
                        TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                        tested = true;
                    }
                final boolean finalTested = tested;
                annotateTest(() -> assertTrue(finalTested, "Failed to pass trip stop tests, there probably wasn't enough vehicles to conclude test (tested " + route.getVehicles().length + " vehicles)"));
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
