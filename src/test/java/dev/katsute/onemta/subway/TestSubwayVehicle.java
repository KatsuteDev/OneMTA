package dev.katsute.onemta.subway;

import dev.katsute.onemta.attribute.Reference;
import dev.katsute.onemta.types.VehicleValidation;

import static org.junit.jupiter.api.Assertions.*;

final class TestSubwayVehicle {

    static void testVehicles(final Reference.Vehicle<Subway.Vehicle> vehicles){
        VehicleValidation.testVehicles(vehicles);
        for(final Subway.Vehicle vehicle : vehicles.getVehicles())
            testVehicle(vehicle);
    }

    static void testVehicle(final Subway.Vehicle vehicle){
        testTrip(vehicle.getTrip());
        assertNotNull(vehicle.isAssigned());
        assertNotNull(vehicle.isRerouted());
        assertNotNull(vehicle.isSkipStop());
        assertNotNull(vehicle.isTurnTrain());
        assertNotNull(vehicle.getStatus());
        assertNotNull(vehicle.getStopSequence());
        assertNotNull(vehicle.isExpress());
        assertEquals(vehicle.getRouteID().toUpperCase().endsWith("X"), vehicle.isExpress());
    }

    static void testTrip(final Subway.Trip trip){
        assertNotNull(trip.getDirection());
        for(final Subway.TripStop stop : trip.getTripStops())
            testTripStop(stop);
    }

    static void testTripStop(final Subway.TripStop stop){
        assertNotNull(stop.getActualTrack());
        assertNotNull(stop.getTrack());
    }

}