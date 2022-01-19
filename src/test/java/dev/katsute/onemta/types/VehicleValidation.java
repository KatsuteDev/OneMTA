package dev.katsute.onemta.types;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitVehicle
 * @see GTFSVehicle
 */
public abstract class VehicleValidation {

    public static void testVehicle(final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getVehicleID()));
        annotateTest(() -> assertNotNull(vehicle.getStopID()));
        annotateTest(() -> assertNotNull(vehicle.getRouteID()));
    }

    public static void testGTFSVehicle(final GTFSVehicle<?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getCurrentStatus()));
    }

}
