package dev.katsute.onemta.bus;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestBusStop {

    private static OneMTA MTA;

    private static Bus.Stop stop;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> stop = MTA.getBusStop(TestProvider.BUS_STOP));
    }

}
