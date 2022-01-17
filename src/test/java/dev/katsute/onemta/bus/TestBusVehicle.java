package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestBusVehicle {

    private static MTA mta;

    private static Bus.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = mta.getBusRoute(TestProvider.BUS_ROUTE).getVehicles());
    }

}
