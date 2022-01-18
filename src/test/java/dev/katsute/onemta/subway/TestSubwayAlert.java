package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TestTransitAlert;
import dev.katsute.onemta.types.TestTransitStop;
import org.junit.jupiter.api.*;

final class TestSubwayAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            TestTransitAlert.testAlerts(mta.getSubwayAlerts());
        }

    }

}
