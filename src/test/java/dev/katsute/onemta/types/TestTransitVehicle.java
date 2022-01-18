package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.VehiclesReference;
import org.junit.jupiter.api.Assumptions;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

public final class TestTransitVehicle {

    public static void testVehicleReference(final VehiclesReference<?> reference){
        annotateTest(() -> assertDoesNotThrow(reference::getVehicles));
        annotateTest(() -> Assumptions.assumeTrue(reference.getVehicles().length > 0, "Skipping vehicle reference tests, no vehicles found"));

        for(final TransitVehicle<?,?,?,?,?,?> vehicle : reference.getVehicles())
            annotateTest(() -> assertSame(reference, reference instanceof TransitRoute ? vehicle.getRoute() : vehicle.getStop()));
    }

}
