package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Reference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitTrip
 * @see TransitTripStop
 */
public abstract class TripValidation {

    public static void requireTripStops(final TransitTripStop<?,?,?>[] trip){
        assertNotEquals(0, trip.length, "No trip stops found, please rerun tests");
    }

    //

    public static void testTrip(final TransitTrip<?,?,?> trip){
        assertNotNull(trip.getTripStops());
    }

    public static void testTripReference(final Reference.Trip<?> reference){
        assertSame(reference, reference.getTrip().getVehicle());
    }

    public static void testTripRouteReference(final TransitVehicle<?,?,?,?,?,?> reference){
        assertSame(reference.getRoute(), reference.getTrip().getRoute());
    }

    //

    public static void testTripStops(final TransitTripStop<?,?,?>[] trip){
        requireTripStops(trip);
        for(final TransitTripStop<?, ?, ?> stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TransitTripStop<?,?,?> stop){
        assertNotNull(stop.getArrivalTimeEpochMillis());
        assertNotNull(stop.getArrivalTime());
        assertNotNull(stop.getDepartureTimeEpochMillis());
        assertNotNull(stop.getDepartureTime());

        assertNotNull(stop.getStopID());
    }

}
