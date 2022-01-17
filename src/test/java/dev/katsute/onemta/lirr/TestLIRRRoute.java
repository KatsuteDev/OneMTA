package dev.katsute.onemta.lirr;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestLIRRRoute {

    private static OneMTA MTA;

    private static LIRR.Route route;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> route = MTA.getLIRRRoute(TestProvider.LIRR_ROUTE));
    }

}
