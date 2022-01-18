package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestLIRRRoute {

    private static MTA mta;

    private static LIRR.Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getLIRRRoute(TestProvider.LIRR_ROUTE));
    }

}
