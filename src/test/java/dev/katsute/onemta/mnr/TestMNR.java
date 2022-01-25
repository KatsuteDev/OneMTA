package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestMNR {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testStopCode(){
        annotateTest(() -> assertEquals(TestProvider.MNR_STOP, mta.getMNRStop(TestProvider.MNR_STOP_CODE).getStopID()));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            annotateTest(() -> assumeTrue(mta.getMNRAlerts().length > 0, "No alerts found, skipping alert tests"));
            for(final MNR.Alert alert : mta.getMNRAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
