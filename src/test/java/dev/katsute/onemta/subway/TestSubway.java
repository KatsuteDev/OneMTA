package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestSubway {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @ParameterizedTest
    @ValueSource(strings={"A","C","E","SF","SR","B","D","F","FX","M","G","J","Z","N","Q","R","W","L","1","2","3","4","5","6","6X","7","7X","9","GS","S", "SI","SIR"})
    final void testFeed(final String route){
        annotateTest(() -> assertDoesNotThrow(() -> mta.getSubwayRoute(route)));
    }

    @Test
    final void testRoute(){
        annotateTest(() -> assertEquals(TestProvider.SUBWAY_ROUTE, mta.getSubwayRoute(Integer.parseInt(TestProvider.SUBWAY_ROUTE)).getRouteID()));
    }

    @Test
    final void testStop(){
        annotateTest(() -> assertEquals(TestProvider.SUBWAY_STOP, mta.getSubwayStop(Integer.parseInt(TestProvider.SUBWAY_STOP)).getStopID()));
    }

}
