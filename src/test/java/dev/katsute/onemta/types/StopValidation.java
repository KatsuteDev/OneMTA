package dev.katsute.onemta.types;

import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.*;
import dev.katsute.onemta.subway.Subway;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitStop
 * @see RailroadStop
 */
public abstract class StopValidation {

    public static void testStop(final TransitStop<?,?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopID()));
        annotateTest(() -> assertNotNull(stop.getStopName()));

        annotateTest(() -> assertNotNull(stop.getLatitude()));
        annotateTest(() -> assertNotNull(stop.getLongitude()));

        /* test refresh */ {
            TransitStop<?,?,?> temp;

            if(stop instanceof Bus.Stop)
                temp = TestProvider.mta.getBusStop(((Bus.Stop) stop).getStopID());
            else if(stop instanceof Subway.Stop)
                temp = TestProvider.mta.getSubwayStop(((Subway.Stop) stop).getStopID());
            else if(stop instanceof LIRR.Stop)
                temp = TestProvider.mta.getLIRRStop(((LIRR.Stop) stop).getStopID());
            else if(stop instanceof MNR.Stop)
                temp = TestProvider.mta.getMNRStop(((MNR.Stop) stop).getStopID());
            else
                return;

            final TransitVehicle<?,?,?,?,?,?>[] vehicles = temp.getVehicles();
            final TransitAlert<?,?,?,?>[] alerts = temp.getAlerts();

            temp.refresh();

            annotateTest(() -> assertNotSame(vehicles, temp.getVehicles()));
            annotateTest(() -> assertNotSame(alerts, temp.getAlerts()));
        }
    }

    public static void testRailroadStop(final RailroadStop<?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopCode()));
        annotateTest(() -> assertNotNull(stop.getStopDescription()));
        annotateTest(() -> assertNotNull(stop.hasWheelchairBoarding()));
    }

}
