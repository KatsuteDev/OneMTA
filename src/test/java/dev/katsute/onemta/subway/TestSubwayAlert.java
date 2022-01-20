package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestSubwayAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("subway");
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            annotateTest(() -> assumeTrue(mta.getSubwayAlerts().length > 0, "No alerts found, skipping alert tests"));
            for(final Alert alert : mta.getSubwayAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
