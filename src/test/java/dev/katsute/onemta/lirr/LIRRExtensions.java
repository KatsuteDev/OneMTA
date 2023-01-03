package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;

import static dev.katsute.onemta.railroad.LIRR.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class LIRRExtensions {

    public static void testVehicle(final Vehicle vehicle){
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());

        assertNotNull(vehicle.getBearing());
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        assertEquals(vehicle.getVehicleID(), mta.getLIRRTrain(vehicle.getVehicleID()).getVehicleID());
    }

    //

    public static void testTrip(final Trip trip){
        assertNotNull(trip.getDirection());
    }

}
