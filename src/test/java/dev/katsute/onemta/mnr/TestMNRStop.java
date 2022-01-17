package dev.katsute.onemta.mnr;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRStop {

    private static OneMTA MTA;

    private static MNR.Stop stop;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> stop = MTA.getMNRStop(TestProvider.MNR_STOP));
    }

}
