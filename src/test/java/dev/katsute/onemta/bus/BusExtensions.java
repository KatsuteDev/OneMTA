package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
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
    }

    public static void testVehicleNumber(final MTA mta, final Vehicle vehicle){
        assertEquals(vehicle.getVehicleID(), mta.getBus(vehicle.getVehicleID()).getVehicleID());
    }

    //

    public static void testTripStops(final TripStop[] trip){
        TripValidation.requireTripStops(trip);
        TripValidation.testTripStops(trip);
    }
}
