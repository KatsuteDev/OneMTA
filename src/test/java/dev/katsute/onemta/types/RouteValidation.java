package dev.katsute.onemta.types;

import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitRoute
 * @see TransitAgency
 */
public abstract class RouteValidation {

    public static void testRoute(final TransitRoute<?,?,?> route){
        testAgency(route.getAgency());

        assertNotNull(route.getRouteID());
        assertNotNull(route.getRouteName());
        assertNotNull(route.getRouteColor());
        assertNotNull(route.getRouteTextColor());

        /* test refresh */ {
            TransitRoute<?,?,?> temp;

            if(route instanceof Bus.Route)
                temp = TestProvider.mta.getBusRoute(((Bus.Route) route).getRouteID());
            else if(route instanceof Subway.Route)
                temp = TestProvider.mta.getSubwayRoute(((Subway.Route) route).getRouteID());
            else if(route instanceof LIRR.Route)
                temp = TestProvider.mta.getLIRRRoute(((LIRR.Route) route).getRouteID());
            else if(route instanceof MNR.Route)
                temp = TestProvider.mta.getMNRRoute(((MNR.Route) route).getRouteID());
            else
                return;

            final TransitVehicle<?,?,?,?,?,?>[] vehicles = temp.getVehicles();
            final TransitAlert<?,?,?,?>[] alerts = temp.getAlerts();

            temp.refresh();

            assertNotSame(vehicles, temp.getVehicles());
            assertNotSame(alerts, temp.getAlerts());
        }
    }

    //

    private static void testAgency(final TransitAgency agency){
        assertNotNull(agency.getAgencyID());
        assertNotNull(agency.getAgencyName());
    }

}
