package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.types.TripValidation;

import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class SubwayExtensions {

    public static void testVehicle(final Vehicle vehicle){
        assertEquals(vehicle.getRouteID().toUpperCase().endsWith("X"), vehicle.isExpress());
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        assertEquals(vehicle.getVehicleID(), mta.getSubwayTrain(vehicle.getVehicleID()).getVehicleID());
    }

    //

    public static void testTrip(final Trip trip){
        assertNotNull(trip.getDirection());
    }

    //

    public static void testTripStops(final TripStop[] trip){
        for(final TripStop stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TripStop trip){
        assertNotNull(trip.getActualTrack());
    }

}
