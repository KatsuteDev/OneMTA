package dev.katsute.onemta.subway;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayStop {

    private static OneMTA MTA;

    private static Subway.Stop stop;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> stop = MTA.getSubwayStop(TestProvider.SUBWAY_STOP));
    }

}
