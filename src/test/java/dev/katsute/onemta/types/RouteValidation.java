package dev.katsute.onemta.types;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitRoute
 * @see TransitAgency
 */
public abstract class RouteValidation {

    public static void testRoute(final TransitRoute<?,?,?> route){
        AgencyValidation.testAgency(route.getAgency());
        assertNotNull(route.getRouteID());
        assertNotNull(route.getRouteName());
        assertNotNull(route.getRouteColor());
        assertNotNull(route.getRouteTextColor());
        assertDoesNotThrow(route::refresh);
    }

}