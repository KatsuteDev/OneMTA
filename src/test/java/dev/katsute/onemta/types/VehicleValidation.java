package dev.katsute.onemta.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitVehicle
 * @see GTFSVehicle
 */
public abstract class VehicleValidation {

    public static void testVehicles(final TransitVehicle<?,?,?,?,?,?>[] vehicles){
        annotateTest(() -> Assertions.assertNotNull(vehicles));
        annotateTest(() -> Assumptions.assumeTrue(vehicles.length > 0, "Skipping vehicle reference tests, no vehicles found"));

        for(final TransitVehicle<?,?,?,?,?,?> vehicle : vehicles)
            testVehicle(vehicle);
    }

    public static void testGTFSVehicles(final GTFSVehicle<?,?,?,?,?>[] vehicles){
        annotateTest(() -> Assertions.assertNotNull(vehicles));
        annotateTest(() -> Assumptions.assumeTrue(vehicles.length > 0, "Skipping vehicle reference tests, no vehicles found"));

        for(final GTFSVehicle<?,?,?,?,?> vehicle : vehicles)
            testGTFSVehicle(vehicle);
    }

    private static void testVehicle(final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getVehicleID()));
        annotateTest(() -> assertNotNull(vehicle.getStopID()));
        annotateTest(() -> assertNotNull(vehicle.getRouteID()));
    }

    private static void testGTFSVehicle(final GTFSVehicle<?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getCurrentStatus()));
    }

}
