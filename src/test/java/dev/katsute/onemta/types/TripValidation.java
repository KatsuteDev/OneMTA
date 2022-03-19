package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.TripReference;
import dev.katsute.onemta.railroad.RailroadTripStop;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitTrip
 * @see TransitTripStop
 * @see GTFSTransitTrip
 * @see GTFSTransitTripStop
 * @see RailroadTripStop
 */
public abstract class TripValidation {

    public static void requireTripStops(final TransitTripStop<?,?,?>[] trip){
        assertNotEquals(0, trip.length, "No trip stops found, please rerun tests");
    }

    //

    public static void testTrip(final TransitTrip<?,?,?> trip){
        annotateTest(() -> assertNotNull(trip.getTripStops()));
    }

    public static void testTripReference(final TripReference<?> reference){
        annotateTest(() -> assertSame(reference, reference.getTrip().getVehicle()));
    }

    public static void testTripRouteReference(final TransitVehicle<?,?,?,?,?,?> reference){
        annotateTest(() -> assertSame(reference.getRoute(), reference.getTrip().getRoute()));
    }

    public static void testGTFSTrip(final GTFSTransitTrip<?,?,?> trip){
        annotateTest(() -> assertNotNull(trip.getTripID()));
        annotateTest(() -> assertNotNull(trip.getRouteID()));
    }

    //

    public static void testTripStops(final TransitTripStop<?,?,?>[] trip){
        annotateTest(() -> requireTripStops(trip));
        for(final TransitTripStop<?, ?, ?> stop : trip)
            testTripStop(stop);
    }

    public static void testGTFSTripStops(final GTFSTransitTripStop<?,?,?>[] trip){
        annotateTest(() -> requireTripStops(trip));
        for(final GTFSTransitTripStop<?, ?, ?> stop : trip)
            testGTFSTripStop(stop);
    }

    public static void testRailroadTripStops(final RailroadTripStop<?,?>[] trip){
        annotateTest(() -> requireTripStops(trip));
        for(final RailroadTripStop<?,?> stop : trip)
            testRailroadTripStop(stop);
    }

    private static void testTripStop(final TransitTripStop<?,?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopID()));
    }

    private static void testGTFSTripStop(final GTFSTransitTripStop<?,?,?> stop){
        annotateTest(() -> assertNotNull(stop.getArrivalTimeEpochMillis()));
        annotateTest(() -> assertNotNull(stop.getArrivalTime()));
        annotateTest(() -> assertNotNull(stop.getDepartureTimeEpochMillis()));
        annotateTest(() -> assertNotNull(stop.getDepartureTime()));
        annotateTest(() -> assertNotNull(stop.getTrack()));
    }

    private static void testRailroadTripStop(final RailroadTripStop<?,?> stop){
        annotateTest(() -> assertNotNull(stop.getDelay()));
        annotateTest(() -> assertNotNull(stop.getTrainStatus()));
    }

}
