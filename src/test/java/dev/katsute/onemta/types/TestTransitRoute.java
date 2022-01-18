package dev.katsute.onemta.types;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

public final class TestTransitRoute {

    public static void testRoute(final TransitRoute<?,?,?> route){
        testAgency(route.getAgency());

        annotateTest(() -> assertNotNull(route.getRouteID()));
        annotateTest(() -> assertNotNull(route.getRouteName()));
        annotateTest(() -> assertNotNull(route.getRouteColor()));
        annotateTest(() -> assertNotNull(route.getRouteTextColor()));

        TestTransitVehicle.testVehicleReference(route);
        TestTransitAlert.testAlerts(route);
    }

    private static void testAgency(final TransitAgency agency){
        annotateTest(() -> assertNotNull(agency.getAgencyID()));
        annotateTest(() -> assertNotNull(agency.getAgencyName()));
    }

}
