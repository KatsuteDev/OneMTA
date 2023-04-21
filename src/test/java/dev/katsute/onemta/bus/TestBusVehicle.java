package dev.katsute.onemta.bus;

import dev.katsute.onemta.attribute.Reference;
import dev.katsute.onemta.types.VehicleValidation;

import static org.junit.jupiter.api.Assertions.*;

final class TestBusVehicle {

    static void testVehicles(final Reference.Vehicle<Bus.Vehicle> vehicles){
        VehicleValidation.testVehicles(vehicles);
        for(final Bus.Vehicle vehicle : vehicles.getVehicles())
            testVehicle(vehicle);
    }

    static void testVehicle(final Bus.Vehicle vehicle){
        testTrip(vehicle.getTrip());
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());
        assertNotNull(vehicle.getBearing());

        assertNotNull(vehicle.getDirection());

        assertNotNull(vehicle.isSelectBusService());
        assertNotNull(vehicle.isExpress());
        assertNotNull(vehicle.isShuttle());
        assertNotNull(vehicle.isLimited());

        assertNotNull(vehicle.getPassengers());
    }

    static void testTrip(final Bus.Trip trip){
        assertNotNull(trip.getDelay());
        for(final Bus.TripStop stop : trip.getTripStops())
            testTripStop(stop);
    }

    static void testTripStop(final Bus.TripStop stop){
        assertNotNull(stop.getStopSequence());
    }

}