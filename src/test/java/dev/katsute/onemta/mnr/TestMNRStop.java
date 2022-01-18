package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRStop {

    private static MTA mta;

    private static MNR.Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getMNRStop(TestProvider.MNR_STOP));
    }

}
