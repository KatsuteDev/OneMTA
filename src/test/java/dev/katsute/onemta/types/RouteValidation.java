package dev.katsute.onemta.types;

import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

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

            annotateTest(() -> assertNotSame(vehicles, temp.getVehicles()));
            annotateTest(() -> assertNotSame(alerts, temp.getAlerts()));
        }
    }

    //

    private static void testAgency(final TransitAgency agency){
        annotateTest(() -> assertNotNull(agency.getAgencyID()));
        annotateTest(() -> assertNotNull(agency.getAgencyName()));
    }

}
