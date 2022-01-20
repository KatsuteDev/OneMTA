package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

abstract class SubwayExtensions {

    public static void testRoute(final Route route){
        annotateTest(() -> assertNotNull(route.getRouteShortName()));
        annotateTest(() -> assertNotNull(route.getRouteDescription()));
    }

    //

    public static void testStop(final Stop stop){
        if(stop.getStopID().endsWith("N") || stop.getStopID().endsWith("S"))
            annotateTest(() -> assertNotNull(stop.getDirection()));
    }

    //

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        annotateTest(() -> assertEquals(vehicle.getVehicleID(), mta.getSubwayTrain(vehicle.getVehicleID()).getVehicleID()));
    }

    //

    public static void testTrip(final Trip trip){
        annotateTest(() -> assertNotNull(trip.getDirection()));
    }

    //

    public static void testTripStops(final TripStop[] trip){
        annotateTest(() -> assumeTrue(trip.length > 0, "No trip stops found, skipping tests"));
        for(final TripStop stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TripStop trip){
        annotateTest(() -> assertNotNull(trip.getActualTrack()));
    }

}
