package dev.katsute.onemta.mnr;

import dev.katsute.onemta.attribute.Reference;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.VehicleValidation;

import static org.junit.jupiter.api.Assertions.*;

final class TestMNRVehicle {

    static void testVehicles(final Reference.Vehicle<MNR.Vehicle> vehicles){
        VehicleValidation.testVehicles(vehicles);
        for(final MNR.Vehicle vehicle : vehicles.getVehicles())
            testVehicle(vehicle);
    }

    static void testVehicle(final MNR.Vehicle vehicle){
        testTrip(vehicle.getTrip());
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());

        assertNotNull(vehicle.getStatus());
    }

    static void testTrip(final MNR.Trip trip){
        for(final MNR.TripStop stop : trip.getTripStops())
            testTripStop(stop);
    }

    static void testTripStop(final MNR.TripStop stop){
        assertNotNull(stop.getDelay());
        assertNotNull(stop.getStatus());
        assertNotNull(stop.getTrack());
    }

}
