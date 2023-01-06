package dev.katsute.onemta.types;

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
