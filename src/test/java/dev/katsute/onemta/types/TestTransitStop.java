package dev.katsute.onemta.types;

import dev.katsute.onemta.railroad.RailroadStop;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

public final class TestTransitStop {

    public static void testStop(final TransitStop<?,?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopID()));
        annotateTest(() -> assertNotNull(stop.getStopName()));

        annotateTest(() -> assertNotNull(stop.getLatitude()));
        annotateTest(() -> assertNotNull(stop.getLongitude()));

        TestTransitVehicle.testVehicleReference(stop);
        TestTransitAlert.testAlerts(stop);
    }

    public static void testRailroadStop(final RailroadStop<?,?> stop){
        annotateTest(() -> assertNotNull(stop.getStopCode()));
        annotateTest(() -> assertNotNull(stop.getStopDescription()));
        annotateTest(() -> assertNotNull(stop.hasWheelchairBoarding()));
    }

}
