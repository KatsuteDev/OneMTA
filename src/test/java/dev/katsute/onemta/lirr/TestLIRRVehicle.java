package dev.katsute.onemta.lirr;

import dev.katsute.onemta.attribute.Reference;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.types.TripValidation;
import dev.katsute.onemta.types.VehicleValidation;

import static org.junit.jupiter.api.Assertions.*;

final class TestLIRRVehicle {

    static void testVehicles(final Reference.Vehicle<LIRR.Vehicle> vehicles){
        VehicleValidation.testVehicles(vehicles);
        for(final LIRR.Vehicle vehicle : vehicles.getVehicles())
            testVehicle(vehicle);
    }

    static void testVehicle(final LIRR.Vehicle vehicle){
        testTrip(vehicle.getTrip());
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());
        assertNotNull(vehicle.getBearing());

        assertNotNull(vehicle.getStatus());
    }

    static void testTrip(final LIRR.Trip trip){
        TripValidation.testTrip(trip);
        assertNotNull(trip.getDirection());
        assertNotNull(trip.getScheduleRelationship());
        for(final LIRR.TripStop stop : trip.getTripStops())
            testTripStop(stop);
    }

    static void testTripStop(final LIRR.TripStop stop){
        assertNotNull(stop.getStopSequence());
        assertNotNull(stop.getDelay());
        assertNotNull(stop.getScheduleRelationship());
        assertNotNull(stop.getTrack());
        assertNotNull(stop.getStatus());
    }

}
