package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TestTransitRoute;
import dev.katsute.onemta.types.TestTransitStop;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayStop {

    private static MTA mta;

    private static Subway.Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getSubwayStop(TestProvider.SUBWAY_STOP));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitStop(){
            TestTransitStop.testStop(stop);
        }

    }

}
