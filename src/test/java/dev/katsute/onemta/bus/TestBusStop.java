package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBusStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getBusStop(TestProvider.BUS_STOP));
    }

    @ParameterizedTest(name="[{index}] {0}")
    @MethodSource("methodProvider")
    final void testMethods(final String name, final Function<Stop,Object> function){
        annotateTest(() -> assertNotNull(function.apply(stop), "Expected " + name + " to not be null"));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> methodProvider(){
        return new TestProvider.MethodStream<Stop>()
            .add("StopID", Stop::getStopID)
            .add("StopName", Stop::getStopName)
            .add("RouteDescription", Stop::getRouteDescription)
            .add("Latitude", Stop::getLatitude)
            .add("Longitude", Stop::getLongitude)
            .stream();
    }

    @Nested
    final class TestVehicle {

        @Test
        final void testVehicleFilter(){
            annotateTest(() -> {
                assumeTrue(stop.getVehicles().length > 0, "There were no vehicles for stop " + TestProvider.BUS_STOP + ", vehicle test was skipped");
                for(final Vehicle vehicle : stop.getVehicles())
                    assertEquals(TestProvider.BUS_STOP, vehicle.getStopID());
            });
        }

    }

    @Nested
    final class TestAlert {

        @Test
        final void testAlertFilter(){
            annotateTest(() -> {
                assumeTrue(stop.getAlerts().length > 0, "There were no alerts for stop " + TestProvider.BUS_STOP + ", alert test was skipped");
                for(final Alert alert : stop.getAlerts())
                    assertTrue(Arrays.asList(alert.getStopIDs()).contains(TestProvider.BUS_STOP));
            });
        }

    }

}
