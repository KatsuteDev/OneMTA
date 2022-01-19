package dev.katsute.onemta.mnr;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class MNRExtensions {

    public static void testVehicle(final Vehicle vehicle){
        annotateTest(() -> assertNotNull(vehicle.getLatitude()));
        annotateTest(() -> assertNotNull(vehicle.getLongitude()));
    }

}
