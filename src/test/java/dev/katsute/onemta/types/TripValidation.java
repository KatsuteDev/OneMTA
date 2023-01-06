package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Reference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitTrip
 * @see TransitTripStop
 */
public abstract class TripValidation {

    public static void testTrip(final Reference.Trip<?> trip){
        testTrip(trip.getTrip());
    }

    public static void testTrip(final TransitTrip<?,?,?,?> trip){
        assertNotNull(trip.getTripID());
        assertNotNull(trip.getTripID());
    }

    public static void testTripStops(final TransitTrip<?,?,?,?> trip){
        testTripStops(trip.getTripStops());
        for(final TransitTripStop<?,?,?> stop : trip.getTripStops())
            assertSame(trip, stop.getTrip());
    }

    public static void testTripStops(final TransitTripStop<?,?,?>[] stops){
        assertNotNull(stops);
        assertNotEquals(0, stops.length);
        for(final TransitTripStop<?,?,?> stop : stops)
            testTripStop(stop);
    }

    public static void testTripStop(final TransitTripStop<?,?,?> stop){
        assertNotNull(stop.getStopID());

        assertNotNull(stop.getArrivalTimeEpochMillis());
        assertNotNull(stop.getArrivalTime());
        assertEquals(stop.getArrivalTimeEpochMillis(), stop.getArrivalTime().getTime());
        assertNotNull(stop.getDepartureTimeEpochMillis());
        assertNotNull(stop.getDepartureTime());
        assertEquals(stop.getDepartureTimeEpochMillis(), stop.getDepartureTime().getTime());

        assertNotNull(stop.getStopID());
    }

    //

    public static void testTripReference(final Reference.Trip<?> reference){
        assertSame(reference, reference.getTrip().getVehicle());
    }

    public static void testTripRouteReference(final TransitVehicle<?,?,?,?,?,?> reference){
        assertSame(reference.getRoute(), reference.getTrip().getRoute());
    }

}
