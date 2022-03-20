package dev.katsute.onemta.types;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitRoute
 * @see TransitAgency
 */
public abstract class RouteValidation {

    public static void testRoute(final TransitRoute<?,?,?> route){
        testAgency(route.getAgency());

        annotateTest(() -> assertNotNull(route.getRouteID()));
        annotateTest(() -> assertNotNull(route.getRouteName()));
        annotateTest(() -> assertNotNull(route.getRouteColor()));
        annotateTest(() -> assertNotNull(route.getRouteTextColor()));

        /* test refresh */ {
            final TransitVehicle<?, ?, ?, ?, ?, ?>[] vehicles = route.getVehicles();
            final TransitAlert<?, ?, ?, ?>[] alerts = route.getAlerts();

            route.refresh();

            annotateTest(() -> assertNotSame(vehicles, route.getVehicles()));
            annotateTest(() -> assertNotSame(alerts, route.getAlerts()));
        }
    }

    //

    private static void testAgency(final TransitAgency agency){
        annotateTest(() -> assertNotNull(agency.getAgencyID()));
        annotateTest(() -> assertNotNull(agency.getAgencyName()));
    }

}
