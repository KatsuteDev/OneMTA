package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.types.TripValidation;

import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class SubwayExtensions {

    public static void testRoute(final Route route){
        assertNotNull(route.getRouteShortName());
        assertNotNull(route.getRouteDescription());
    }

    //

    public static void testStop(final Stop stop){
        if(stop.getStopID().endsWith("N") || stop.getStopID().endsWith("S"))
            assertNotNull(stop.getDirection());
    }

    //

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
        TripValidation.requireTripStops(trip);
        for(final TripStop stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TripStop trip){
        assertNotNull(trip.getActualTrack());
    }

}
