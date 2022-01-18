package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.TestTransitRoute;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;

final class TestMNRRoute {

    private static MTA mta;

    private static MNR.Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getMNRRoute(TestProvider.MNR_ROUTE));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitRoute(){
            TestTransitRoute.testRoute(route);
        }

    }

}
