package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestSubway {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("subway");
        mta = TestProvider.getOneMTA();
    }

    @ParameterizedTest
    @ValueSource(strings={"A","C","E","SF","SR","B","D","F","FX","M","G","J","Z","N","Q","R","W","L","1","2","3","4","5","6","6X","7","7X","9","GS","S", "SI","SIR"})
    final void testFeed(final String route){
        assertDoesNotThrow(() -> mta.getSubwayRoute(route));
    }

    @Test
    final void testRoute(){
        assertEquals(TestProvider.SUBWAY_ROUTE, mta.getSubwayRoute(Integer.parseInt(TestProvider.SUBWAY_ROUTE)).getRouteID());
    }

    @Test
    final void testStop(){
        assertEquals(TestProvider.SUBWAY_STOP, mta.getSubwayStop(Integer.parseInt(TestProvider.SUBWAY_STOP)).getStopID());
    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitAlert(){
            assumeTrue(mta.getSubwayAlerts().length > 0, "No alerts found, skipping alert tests");

            { // missing description caused by MTA missing data
                assertTrue(TestProvider.atleastOneTrue(
                    mta.getSubwayAlerts(), Subway.Alert.class,
                    a -> a.getDescription() != null
                ));
            }

            for(final Subway.Alert alert : mta.getSubwayAlerts())
                AlertValidation.testAlert(alert);
        }

    }

}
