package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBusAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("bus");
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            annotateTest(() -> assumeTrue(mta.getBusAlerts().length > 0, "No alerts found, skipping alert tests"));
            for(final Alert alert : mta.getBusAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
