package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestSubwayRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("subway");

        annotateTest(() -> route = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE));
    }

    @Nested
    final class ExtensionTests {

        @Test
        final void testStop(){
            SubwayExtensions.testRoute(route);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testID(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                SubwayExtensions.testVehicleNumber(mta, route.getVehicles()[0]);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    SubwayExtensions.testTrip(vehicle.getTrip());
                }
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testGTFSTransitVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testGTFSVehicle(vehicle);
            }

            @Test
            final void testVehicleRouteReference(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicleRouteReference(route, vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testGTFSVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testGTFSTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripRouteReference(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripRouteReference(vehicle);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                }
            }

            @Test
            final void testGTFSTripStops(){
                annotateTest(() -> VehicleValidation.requireVehicles(route));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
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
