package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

final class TestMNRAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            // this test may sometimes fail due to the MTA reporting a route ID that doesn't exist (external issue)
            AlertValidation.testAlerts(mta.getMNRAlerts());
        }

    }

}
