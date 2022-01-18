package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.types.TestTransitStop;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;

final class TestLIRRStop {

    private static MTA mta;

    private static LIRR.Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getLIRRStop(TestProvider.LIRR_STOP));
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
