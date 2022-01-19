package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

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

    @Test
    final void testMethods(){
        annotateTest(() -> assertNotNull(route.getRouteShortName()));
        annotateTest(() -> assertNotNull(route.getRouteDescription()));
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
    final class InheritedTests {

        @Test
        final void testTransitRoute(){
            RouteValidation.testRoute(route);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testTransitVehicles(){
                annotateTest(() -> Assertions.assertNotNull(route.getVehicles()));
                VehicleValidation.testVehicles(route.getVehicles());
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> assumeTrue(route.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : route.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                }
            }

        }

        @Nested
        final class AlertTests {

            @Test
            final void testTransitAlerts(){
                AlertValidation.testAlerts(route);
            }

        }

    }

}
