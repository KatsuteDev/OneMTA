package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRVehicle {

    private static MTA mta;

    private static MNR.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = mta.getMNRRoute(TestProvider.MNR_ROUTE).getVehicles());
    }

}
