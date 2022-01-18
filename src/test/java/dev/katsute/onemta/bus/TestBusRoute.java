package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TestTransitRoute;
import dev.katsute.onemta.types.TransitRoute;
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

final class TestBusRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> route = mta.getBusRoute(TestProvider.BUS_ROUTE));
    }

    @ParameterizedTest(name="[{index}] {0}")
    @MethodSource("methodProvider")
    final void testMethods(final String name, final Function<Route,Object> function){
        annotateTest(() -> assertNotNull(function.apply(route), "Expected " + name + " to not be null"));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> methodProvider(){
        return new TestProvider.MethodStream<Route>()
            .add("RouteID", Route::getRouteID)
            .add("RouteShortName", Route::getRouteShortName)
            .add("RouteName", Route::getRouteName)
            .add("RouteDescription", Route::getRouteDescription)
            .add("RouteColor", Route::getRouteColor)
            .add("RouteTextColor", Route::getRouteTextColor)
            .add("+SelectBusService", Route::isSelectBusService)
            .add("ExpressBus", Route::isExpress)
            .add("ShuttleBus", Route::isShuttle)
            .add("LimitedBus", Route::isLimited)
            .add("Agency", TransitRoute::getAgency)
            .add("Agency#AgencyID", route -> route.getAgency().getAgencyID())
            .add("Agency#AgencyName", route -> route.getAgency().getAgencyName())
            .stream();
    }

    @Nested
    final class TestBusType {

        @Test
        final void testSelectBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("B44+").isSelectBusService()));
        }

        @Test
        final void testExpressBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("SIM1").isExpress()));
        }

        @Test
        final void testShuttleBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("F1").isShuttle()));
        }

        @Test
        final void testLimitedBus(){
            annotateTest(() -> assertTrue(mta.getBusRoute("Q44+").isLimited()));
        }

    }

    @Nested
    final class TestVehicle {

        @Test
        final void testVehicleFilter(){
            annotateTest(() -> {
                assumeTrue(route.getVehicles().length > 0, "There were no vehicles for route " + TestProvider.BUS_ROUTE + ", vehicle test was skipped");
                for(final Vehicle vehicle : route.getVehicles())
                    assertEquals(TestProvider.BUS_ROUTE, vehicle.getRouteID());
            });
        }

    }

    @Nested
    final class TestAlert {

        @Test
        final void testAlertFilter(){
            annotateTest(() -> {
                assumeTrue(route.getAlerts().length > 0, "There were no alerts for route " + TestProvider.BUS_ROUTE + ", alert test was skipped");
                for(final Alert alert : route.getAlerts())
                    assertTrue(Arrays.asList(alert.getRouteIDs()).contains(TestProvider.BUS_ROUTE));
            });
        }

    }

    //

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitRoute(){
            TestTransitRoute.testRoute(route);
        }

    }

}
