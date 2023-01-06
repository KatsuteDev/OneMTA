package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Reference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitVehicle
 */
public abstract class VehicleValidation {

    public static void testVehicles(final Reference.Vehicle<?> vehicles){
        testVehicles(vehicles.getVehicles());
    }

    public static void testVehicles(final TransitVehicle<?,?,?,?,?,?>[] vehicles){
        assertNotNull(vehicles);
        assertNotEquals(0, vehicles.length);
        for(final TransitVehicle<?,?,?,?,?,?> vehicle : vehicles)
            testVehicle(vehicle);
    }

    private static void testVehicle(final TransitVehicle<?,?,?,?,?,?> vehicle){
        assertNotNull(vehicle.getVehicleID());
        assertNotNull(vehicle.getStopID());
        assertNotNull(vehicle.getRouteID());
        assertDoesNotThrow(vehicle::refresh);
    }

}
