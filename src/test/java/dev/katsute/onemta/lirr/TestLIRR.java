package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestLIRR {

    private static MTA mta;

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("lirr");
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testStopCode(){
        annotateTest(() -> assertEquals(TestProvider.LIRR_STOP, mta.getLIRRStop(TestProvider.LIRR_STOP_CODE).getStopID()));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            annotateTest(() -> assumeTrue(mta.getLIRRAlerts().length > 0, "No alerts found, skipping alert tests"));
            for(final LIRR.Alert alert : mta.getLIRRAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
