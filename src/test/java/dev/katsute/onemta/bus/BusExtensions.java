package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TripValidation;

import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class BusExtensions {

    public static void testRoute(final Route route){
        assertNotNull(route.getRouteShortName());
        assertNotNull(route.getRouteDescription());

        assertNotNull(route.isSelectBusService());
        assertNotNull(route.isExpress());
        assertNotNull(route.isShuttle());
        assertNotNull(route.isLimited());
    }

    //

    public static void testVehicle(final Vehicle vehicle){
        assertNotNull(vehicle.getLatitude());
        assertNotNull(vehicle.getLongitude());

        assertNotNull(vehicle.getBearing());

        assertNotNull(vehicle.getDirection());

        assertNotNull(vehicle.isSelectBusService());
        assertNotNull(vehicle.isExpress());
        assertNotNull(vehicle.isShuttle());
        assertNotNull(vehicle.isLimited());

        assertNotNull(vehicle.getOriginStopCode());
        assertNotNull(vehicle.getDestinationName());
        assertNotNull(vehicle.getProgressRate());

        assertNotNull(vehicle.getAimedArrivalTime());
        assertNotNull(vehicle.getAimedArrivalTimeEpochMillis());

        assertNotNull(vehicle.getArrivalProximityText());
        assertNotNull(vehicle.getDistanceFromStop());
        assertNotNull(vehicle.getStopsAway());
        assertNotNull(vehicle.getStopName());
    }

    public static void testOriginStop(final Vehicle vehicle){
        assertEquals(vehicle.getOriginStopCode(), vehicle.getOriginStop().getStopID());
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        assertEquals(vehicle.getVehicleID(), mta.getBus(vehicle.getVehicleID()).getVehicleID());
    }

    //

    public static void testTripStops(final TripStop[] trip){
        TripValidation.requireTripStops(trip);

        {
            // fields may be missing if stop is skipped
            assertTrue(TestProvider.atleastOneTrue(
                trip, Bus.TripStop.class,
                s -> s.getExpectedArrivalTime() != null &&
                    s.getExpectedArrivalTimeEpochMillis() != null &&
                    s.getArrivalProximityText() != null
            ));
        }

        for(final TripStop stop : trip)
            testTripStop(stop);
    }

    private static void testTripStop(final TripStop stop){
        assertNotNull(stop.getDistanceFromStop());
        assertNotNull(stop.getStopsAway());
        assertNotNull(stop.getStopName());
    }

}
