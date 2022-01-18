package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TestTransitRoute;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayRoute {

    private static MTA mta;

    private static Subway.Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE));
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitRoute(){
            TestTransitRoute.testRoute(route);
        }

    }

}
