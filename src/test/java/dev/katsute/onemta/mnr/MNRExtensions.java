package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class MNRExtensions {

    public static void testVehicle(final Vehicle vehicle){
        annotateTest(() -> assertNotNull(vehicle.getLatitude()));
        annotateTest(() -> assertNotNull(vehicle.getLongitude()));
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        annotateTest(() -> assertEquals(vehicle.getVehicleID(), mta.getMNRTrain(vehicle.getVehicleID()).getVehicleID()));
    }

}
