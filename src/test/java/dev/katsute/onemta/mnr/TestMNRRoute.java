package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNRRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();

        route = mta.getMNRRoute(TestProvider.MNR_ROUTE);
        VehicleValidation.requireVehicles(route);

    }

    @Nested
    final class ComparatorTests {

        @Test
        final void testNotExact(){
            assertFalse(route.isExactRoute(null));
            assertFalse(route.isExactRoute(999));
            assertFalse(route.isExactRoute("999"));
        }

        @Test
        final void testExact(){
            assertTrue(route.isExactRoute(route));
            assertTrue(route.isExactRoute(mta.getMNRRoute(TestProvider.MNR_ROUTE)));
            assertTrue(route.isExactRoute(TestProvider.MNR_ROUTE));
            assertTrue(route.isExactRoute(String.valueOf(TestProvider.MNR_ROUTE)));
        }

    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // not all MNR vehicles have stops for some reason
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getVehicles(), MNR.Vehicle.class,
                        v -> v.getStopID() != null
                    ));
                }

                for(final Vehicle vehicle : route.getVehicles())
                    MNRExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                MNRExtensions.testVehicleNumber(mta, route.getVehicles()[0]);
            }

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
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testVehicleRouteReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    VehicleValidation.testVehicleRouteReference(route, vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                for(final Vehicle vehicle : route.getVehicles()){
                    assertNotNull(vehicle.getTrip());
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testVehicleTripRouteReference(){
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripRouteReference(vehicle);
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                for(final Vehicle vehicle : route.getVehicles())
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
            }

        }

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(route);
        }

    }

}
