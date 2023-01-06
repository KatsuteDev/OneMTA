package dev.katsute.onemta.types;

import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitStop
 */
public abstract class StopValidation {

    public static void testStop(final TransitStop<?,?,?> stop){
        assertNotNull(stop.getStopID());
        assertNotNull(stop.getStopName());

        assertNotNull(stop.getLatitude());
        assertNotNull(stop.getLongitude());
        assertDoesNotThrow(stop::refresh);
    }

}
