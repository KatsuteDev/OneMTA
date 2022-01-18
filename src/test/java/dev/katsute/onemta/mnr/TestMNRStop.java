package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.TestTransitStop;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRStop {

    private static MTA mta;

    private static MNR.Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getMNRStop(TestProvider.MNR_STOP));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitStop(){
            TestTransitStop.testStop(stop);
        }

        @Test
        final void testRailroadStop(){
            TestTransitStop.testRailroadStop(stop);
        }

    }

}
