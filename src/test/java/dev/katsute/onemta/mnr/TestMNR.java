package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(mta.getMNRAlerts());
        }

    }

}
