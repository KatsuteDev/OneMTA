package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBusRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getBusRoute(TestProvider.BUS_ROUTE));
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
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));

                {
                    boolean passes = false;
                    for(final Vehicle vehicle : route.getVehicles()){
                        if(vehicle.getExpectedArrivalTime() != null &&
                           vehicle.getExpectedArrivalTimeEpochMillis() != null &&
                           vehicle.getExpectedDepartureTime() != null &&
                           vehicle.getExpectedDepartureTimeEpochMillis() != null
                        ){
                            passes = true;
                            break;
                        }
                    }

                    final boolean finalPasses = passes;
                    annotateTest(() -> assumeTrue(finalPasses, "Failed to pass expected arrival tests"));
                }

                boolean tested = false;
                for(final Vehicle vehicle : route.getVehicles()){
                    if(!tested && vehicle.getProgressRate().equals("noProgress")){
                        annotateTest(() -> assertNotNull(vehicle.getProgressStatus()));
                        tested = true;
                    }
                    BusExtensions.testVehicle(vehicle);
                }
            }

            @Test
            final void testOrigin(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                BusExtensions.testOriginStop(route.getVehicles()[0]);
            }

            @Test
            final void testID(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                BusExtensions.testVehicleNumber(mta, route.getVehicles()[0]);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                    BusExtensions.testTripStops(vehicle.getTrip().getTripStops());
                }
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
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testVehicleRouteReference(){
                annotateTest(() -> Assumptions.assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicleRouteReference(route, vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testVehicleTripRouteReference(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripRouteReference(vehicle);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                }
            }

        }

        @Nested
        final class AlertTests {

            @Test
            final void testTransitAlerts(){
                annotateTest(() -> assumeTrue(route.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                annotateTest(() -> assumeTrue(route.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlertReference(route, alert);
            }

        }

    }

}
