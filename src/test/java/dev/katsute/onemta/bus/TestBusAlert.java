package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

final class TestBusAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(mta.getBusAlerts());
        }

    }

}
