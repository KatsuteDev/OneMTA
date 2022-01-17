package dev.katsute.onemta.bus;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestBusVehicle {

    private static OneMTA MTA;

    private static Bus.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = MTA.getBusRoute(TestProvider.BUS_ROUTE).getVehicles());
    }

}
