package dev.katsute.onemta.bus;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

abstract class BusExtensions {

    public static void testRoute(final Route route){
        annotateTest(() -> assertNotNull(route.getRouteShortName()));
        annotateTest(() -> assertNotNull(route.getRouteDescription()));

        annotateTest(() -> assertNotNull(route.isSelectBusService()));
        annotateTest(() -> assertNotNull(route.isExpress()));
        annotateTest(() -> assertNotNull(route.isShuttle()));
        annotateTest(() -> assertNotNull(route.isLimited()));
    }

    //

    public static void testStop(final Stop stop){
        annotateTest(() -> assertNotNull(stop.getRouteDescription()));
    }

    //

    public static void testVehicle(final Vehicle vehicle){
        annotateTest(() -> assertNotNull(vehicle.getLatitude()));
        annotateTest(() -> assertNotNull(vehicle.getLongitude()));

        annotateTest(() -> assertNotNull(vehicle.getBearing()));

        annotateTest(() -> assertNotNull(vehicle.getDirection()));

        annotateTest(() -> assertNotNull(vehicle.isSelectBusService()));
        annotateTest(() -> assertNotNull(vehicle.isExpress()));
        annotateTest(() -> assertNotNull(vehicle.isShuttle()));
        annotateTest(() -> assertNotNull(vehicle.isLimited()));

        annotateTest(() -> assertNotNull(vehicle.getOriginStopCode()));
        annotateTest(() -> assertNotNull(vehicle.getDestinationName()));
        annotateTest(() -> assertNotNull(vehicle.getProgressRate()));
        annotateTest(() -> assertNotNull(vehicle.getProgressStatus()));
        annotateTest(() -> assertNotNull(vehicle.getAimedArrivalTime()));
        annotateTest(() -> assertNotNull(vehicle.getAimedArrivalTimeEpochMillis()));
        annotateTest(() -> assertNotNull(vehicle.getExpectedArrivalTime()));
        annotateTest(() -> assertNotNull(vehicle.getExpectedArrivalTimeEpochMillis()));
        annotateTest(() -> assertNotNull(vehicle.getExpectedDepartureTime()));
        annotateTest(() -> assertNotNull(vehicle.getExpectedDepartureTimeEpochMillis()));
        annotateTest(() -> assertNotNull(vehicle.getArrivalProximityText()));
        annotateTest(() -> assertNotNull(vehicle.getDistanceFromStop()));
        annotateTest(() -> assertNotNull(vehicle.getStopsAway()));
        annotateTest(() -> assertNotNull(vehicle.getStopName()));
    }

    //

    public static void testTripStops(final TripStop[] trip){
        annotateTest(() -> assumeTrue(trip.length > 0, "No trip stops found, skipping tests"));
        for(final TripStop stop : trip)
            testTripStop(stop);
    }


    private static void testTripStop(final TripStop stop){
        annotateTest(() -> assertNotNull(stop.getExpectedArrivalTime()));
        annotateTest(() -> assertNotNull(stop.getExpectedArrivalTimeEpochMillis()));
        annotateTest(() -> assertNotNull(stop.getArrivalProximityText()));
        annotateTest(() -> assertNotNull(stop.getDistanceFromStop()));
        annotateTest(() -> assertNotNull(stop.getStopsAway()));
        annotateTest(() -> assertNotNull(stop.getStopName()));
    }

}
