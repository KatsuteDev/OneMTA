package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

final class TestLIRRAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(mta.getLIRRAlerts());
        }

    }

}
