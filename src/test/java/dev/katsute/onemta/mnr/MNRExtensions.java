package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;

import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class MNRExtensions {

    public static void testVehicle(final Vehicle vehicle){
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        assertEquals(vehicle.getVehicleID(), mta.getMNRTrain(vehicle.getVehicleID()).getVehicleID());
    }

}
