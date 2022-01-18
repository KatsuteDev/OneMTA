package dev.katsute.onemta.bus;

import dev.katsute.jcore.Workflow;
import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TransitRoute;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;

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
        Workflow.annotateTest(() -> Assertions.assertNotNull(function.apply(route), "Expected " + name + " to not be null"));
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
            Workflow.annotateTest(() -> Assertions.assertTrue(mta.getBusRoute("B44+").isSelectBusService()));
        }

        @Test
        final void testExpressBus(){
            Workflow.annotateTest(() -> Assertions.assertTrue(mta.getBusRoute("SIM1").isExpress()));
        }

        @Test
        final void testShuttleBus(){
            Workflow.annotateTest(() -> Assertions.assertTrue(mta.getBusRoute("F1").isShuttle()));
        }

        @Test
        final void testLimitedBus(){
            Workflow.annotateTest(() -> Assertions.assertTrue(mta.getBusRoute("Q44+").isLimited()));
        }

    }

    @Nested
    final class TestVehicle {

    }

    @Nested
    final class TestAlert {

        // test only that filter works, alert quality uses separate test class

    }

}
