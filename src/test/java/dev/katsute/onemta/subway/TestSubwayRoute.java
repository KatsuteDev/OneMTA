package dev.katsute.onemta.subway;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayRoute {

    private static OneMTA MTA;

    private static Subway.Route route;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> route = MTA.getSubwayRoute(TestProvider.SUBWAY_ROUTE));
    }

}
