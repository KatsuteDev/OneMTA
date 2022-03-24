package dev.katsute.onemta.types;

import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.attribute.VehiclesReference;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitVehicle
 * @see GTFSVehicle
 */
public abstract class VehicleValidation {

    public static void requireVehicles(final VehiclesReference<?> vehicles){
        assertNotEquals(0, vehicles.getVehicles().length, "No vehicles found, please rerun this test when vehicles are available");
    }

    //

    public static void testVehicle(final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getVehicleID()));
        if(!(vehicle instanceof MNR.Vehicle))
            annotateTest(() -> assertNotNull(vehicle.getStopID()));
        annotateTest(() -> assertNotNull(vehicle.getRouteID()));

        // test refresh
        if(!(vehicle instanceof Bus.Vehicle)){
            final TransitVehicle<?,?,?,?,?,?> temp;
            if(vehicle instanceof Subway.Vehicle)
                temp = TestProvider.mta.getSubwayTrain(((Subway.Vehicle) vehicle).getVehicleID());
            else if(vehicle instanceof LIRR.Vehicle)
                temp = TestProvider.mta.getLIRRTrain(((LIRR.Vehicle) vehicle).getVehicleID());
            else if(vehicle instanceof MNR.Vehicle)
                temp = TestProvider.mta.getMNRTrain(((MNR.Vehicle) vehicle).getVehicleID());
            else
                return;

            final TransitTrip<?,?,?> trip = temp.getTrip();

            temp.refresh();

            annotateTest(() -> assertNotSame(trip, temp.getTrip()));
        }
    }

    public static void testGTFSVehicle(final GTFSVehicle<?,?,?,?,?> vehicle){
        annotateTest(() -> assertNotNull(vehicle.getCurrentStatus()));
    }

    //

    public static void testVehicleRouteReference(final TransitRoute<?,?,?> reference, final TransitVehicle<?,?,?,?,?,?> vehicle){
        annotateTest(() -> assertSame(reference, vehicle.getRoute()));
    }

}
