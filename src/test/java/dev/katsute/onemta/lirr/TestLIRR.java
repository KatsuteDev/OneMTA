package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

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
        assertEquals(TestProvider.LIRR_STOP, mta.getLIRRStop(TestProvider.LIRR_STOP_CODE).getStopID());
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            assumeTrue(mta.getLIRRAlerts().length > 0, "No alerts found, skipping alert tests");

            { // missing description caused by MTA missing data
                assertTrue(TestProvider.atleastOneTrue(
                    mta.getLIRRAlerts(), LIRR.Alert.class,
                    a -> a.getDescription() != null
                ));
            }

            for(final LIRR.Alert alert : mta.getLIRRAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
