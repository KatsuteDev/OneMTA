package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestMNRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getMNRStop(TestProvider.MNR_STOP));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitStop(){
            StopValidation.testStop(stop);
        }

        @Test
        final void testRailroadStop(){
            StopValidation.testRailroadStop(stop);
        }

        //

        @Test
        final void testTransitVehicles(){
            annotateTest(() -> Assertions.assertNotNull(stop.getVehicles()));
            VehicleValidation.testVehicles(stop.getVehicles());
        }

        @Test
        final void testGTFSTransitVehicles(){
            annotateTest(() -> Assertions.assertNotNull(stop.getVehicles()));
            VehicleValidation.testGTFSVehicles(stop.getVehicles());
        }

        //

        @Test
        final void testVehicleTrips(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles()){
                annotateTest(() -> assertNotNull(vehicle.getTrip()));
                TripValidation.testTrip(vehicle.getTrip());
            }
        }

        @Test
        final void testVehicleTripsReference(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles())
                TripValidation.testTripReference(vehicle);
        }

        @Test
        final void testGTFSVehicleTrips(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles()){
                annotateTest(() -> assertNotNull(vehicle.getTrip()));
                TripValidation.testGTFSTrip(vehicle.getTrip());
            }
        }

        //

        @Test
        final void testVehicleTripStops(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles()){
                annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                TripValidation.testTripStops(vehicle.getTrip().getTripStops());
            }
        }

        @Test
        final void testGTFSTripStops(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles()){
                annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
            }
        }

        @Test
        final void testRailroadTripStops(){
            annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
            for(final Vehicle vehicle : stop.getVehicles()){
                annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                TripValidation.testRailroadTripStops(vehicle.getTrip().getTripStops());
            }
        }

        //

        @Test
        final void testTransitAlerts(){
            AlertValidation.testAlerts(stop);
        }

    }

}
