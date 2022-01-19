package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.LIRR.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class LIRRExtensions {

    public static void testVehicle(final Vehicle vehicle){
        annotateTest(() -> assertNotNull(vehicle.getLatitude()));
        annotateTest(() -> assertNotNull(vehicle.getLongitude()));

        annotateTest(() -> assertNotNull(vehicle.getBearing()));
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        annotateTest(() -> assertEquals(vehicle.getVehicleID(), mta.getLIRRTrain(vehicle.getVehicleID()).getVehicleID()));
    }

    //

    public static void testTrip(final Trip trip){
        annotateTest(() -> assertNotNull(trip.getDirection()));
    }

}
