package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;

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

        annotateTest(() -> assertNotNull(vehicle.getAimedArrivalTime()));
        annotateTest(() -> assertNotNull(vehicle.getAimedArrivalTimeEpochMillis()));

        annotateTest(() -> assertNotNull(vehicle.getArrivalProximityText()));
        annotateTest(() -> assertNotNull(vehicle.getDistanceFromStop()));
        annotateTest(() -> assertNotNull(vehicle.getStopsAway()));
        annotateTest(() -> assertNotNull(vehicle.getStopName()));
    }

    public static void testOriginStop(final Vehicle vehicle){
        annotateTest(() -> assertEquals(vehicle.getOriginStopCode(), vehicle.getOriginStop().getStopID()));
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        annotateTest(() -> assertEquals(vehicle.getVehicleID(), mta.getBus(vehicle.getVehicleID()).getVehicleID()));
    }

    //

    public static void testTripStops(final TripStop[] trip){
        annotateTest(() -> assumeTrue(trip.length > 0, "No trip stops found, skipping tests"));

        {
            // fields may be missing if stop is skipped
            boolean tested = false;
            for(final TripStop stop : trip){
                if(stop.getExpectedArrivalTime() != null &&
                   stop.getExpectedArrivalTimeEpochMillis() != null &&
                   stop.getArrivalProximityText() != null){
                    tested = true;
                    break;
                }
            }

            final boolean finalPasses = tested;
            annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests"));
        }

        for(final TripStop stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TripStop stop){
        annotateTest(() -> assertNotNull(stop.getDistanceFromStop()));
        annotateTest(() -> assertNotNull(stop.getStopsAway()));
        annotateTest(() -> assertNotNull(stop.getStopName()));
    }

}
