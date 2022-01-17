package dev.katsute.onemta.mnr;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRVehicle {

    private static OneMTA MTA;

    private static MNR.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = MTA.getMNRRoute(TestProvider.MNR_ROUTE).getVehicles());
    }

}
