package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.TripReference;
import dev.katsute.onemta.railroad.RailroadTripStop;

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
        assertNotNull(trip.getTripStops());
    }

    public static void testTripReference(final TripReference<?> reference){
        assertSame(reference, reference.getTrip().getVehicle());
    }

    public static void testTripRouteReference(final TransitVehicle<?,?,?,?,?,?> reference){
        assertSame(reference.getRoute(), reference.getTrip().getRoute());
    }

    public static void testGTFSTrip(final GTFSTransitTrip<?,?,?> trip){
        assertNotNull(trip.getTripID());
        assertNotNull(trip.getRouteID());
        assertNotNull(trip.getScheduleRelationship());
    }

    //

    public static void testTripStops(final TransitTripStop<?,?,?>[] trip){
        requireTripStops(trip);
        for(final TransitTripStop<?, ?, ?> stop : trip)
            testTripStop(stop);
    }

    public static void testGTFSTripStops(final GTFSTransitTripStop<?,?,?>[] trip){
        requireTripStops(trip);
        for(final GTFSTransitTripStop<?, ?, ?> stop : trip)
            testGTFSTripStop(stop);
    }

    public static void testRailroadTripStops(final RailroadTripStop<?,?>[] trip){
        requireTripStops(trip);
        for(final RailroadTripStop<?,?> stop : trip)
            testRailroadTripStop(stop);
    }

    private static void testTripStop(final TransitTripStop<?,?,?> stop){
        assertNotNull(stop.getStopID());
    }

    private static void testGTFSTripStop(final GTFSTransitTripStop<?,?,?> stop){
        assertNotNull(stop.getArrivalTimeEpochMillis());
        assertNotNull(stop.getArrivalTime());
        assertNotNull(stop.getDepartureTimeEpochMillis());
        assertNotNull(stop.getDepartureTime());
        assertNotNull(stop.getTrack());
    }

    private static void testRailroadTripStop(final RailroadTripStop<?,?> stop){
        assertNotNull(stop.getDelay());
        assertNotNull(stop.getTrainStatus());
    }

}
