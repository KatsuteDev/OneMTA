package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(mta.getLIRRAlerts());
        }

    }

}
