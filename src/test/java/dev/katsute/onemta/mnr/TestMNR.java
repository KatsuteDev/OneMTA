package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

final class TestMNR {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testStopCode(){
        assertEquals(TestProvider.MNR_STOP, mta.getMNRStop(TestProvider.MNR_STOP_CODE).getStopID());
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            assumeTrue(mta.getMNRAlerts().length > 0, "No alerts found, skipping alert tests");

            { // missing description caused by MTA missing data
                assertTrue(TestProvider.atleastOneTrue(
                    mta.getMNRAlerts(), MNR.Alert.class,
                    a -> a.getDescription() != null
                ));
            }

            for(final MNR.Alert alert : mta.getMNRAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
