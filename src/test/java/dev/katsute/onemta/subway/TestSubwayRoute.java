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
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE));
    }

    @Test
    final void testMethods(){
        annotateTest(() -> assertNotNull(route.getRouteShortName()));
        annotateTest(() -> assertNotNull(route.getRouteDescription()));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitRoute(){
            RouteValidation.testRoute(route);
        }

        //

        @Test
        final void testTransitVehicles(){
            annotateTest(() -> assertNotNull(route.getVehicles()));
            VehicleValidation.testVehicles(route.getVehicles());
        }

        @Test
        final void testGTFSTransitVehicles(){
            annotateTest(() -> assertNotNull(route.getVehicles()));
            VehicleValidation.testGTFSVehicles(route.getVehicles());
        }

        //

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
        final void testGTFSVehicleTrips(){
            annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : route.getVehicles()){
                annotateTest(() -> assertNotNull(vehicle.getTrip()));
                TripValidation.testGTFSTrip(vehicle.getTrip());
            }
        }

        //

        @Test
        final void testVehicleTripStops(){
            annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : route.getVehicles()){
                annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                TripValidation.testTripStops(vehicle.getTrip().getTripStops());
            }
        }

        @Test
        final void testGTFSTripStops(){
            annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : route.getVehicles()){
                annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
            }
        }

        //

        @Test
        final void testTransitAlerts(){
            AlertValidation.testAlerts(route);
        }

    }

}
