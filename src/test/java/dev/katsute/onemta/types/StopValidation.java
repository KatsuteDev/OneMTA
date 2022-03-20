package dev.katsute.onemta.types;

import dev.katsute.onemta.railroad.RailroadStop;

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
            final TransitVehicle<?, ?, ?, ?, ?, ?>[] vehicles = stop.getVehicles();
            final TransitAlert<?, ?, ?, ?>[] alerts = stop.getAlerts();

            stop.refresh();

            annotateTest(() -> assertNotSame(vehicles, stop.getVehicles()));
            annotateTest(() -> assertNotSame(alerts, stop.getAlerts()));
        }
    }

    public static void testRailroadStop(final RailroadStop<?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopCode()));
        annotateTest(() -> assertNotNull(stop.getStopDescription()));
        annotateTest(() -> assertNotNull(stop.hasWheelchairBoarding()));
    }

}
