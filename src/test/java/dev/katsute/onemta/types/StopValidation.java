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

            assertNotSame(vehicles, temp.getVehicles());
            assertNotSame(alerts, temp.getAlerts());
        }
    }

}
