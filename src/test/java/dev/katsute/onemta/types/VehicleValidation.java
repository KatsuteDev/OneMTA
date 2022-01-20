package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.VehiclesReference;
import dev.katsute.onemta.railroad.MNR;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @see TransitVehicle
 * @see GTFSVehicle
 */
public abstract class VehicleValidation {

    public static void requireVehicles(final VehiclesReference<?> vehicles){
        // annotate in outer
        assumeTrue(vehicles.getVehicles().length > 0, "No vehicles found, please rerun this test when vehicles are available");
    }

    //

    public static void testVehicle(final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getVehicleID()));
        if(!(vehicle instanceof MNR.Vehicle))
            annotateTest(() -> assertNotNull(vehicle.getStopID()));
        annotateTest(() -> assertNotNull(vehicle.getRouteID()));
    }

    public static void testGTFSVehicle(final GTFSVehicle<?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getCurrentStatus()));
    }

    //

    public static void testVehicleRouteReference(final TransitRoute<?,?,?> reference, final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertSame(reference, vehicle.getRoute()));
    }

}
