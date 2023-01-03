package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.AlertValidation;
import dev.katsute.onemta.types.RouteValidation;
import dev.katsute.onemta.types.TripValidation;
import dev.katsute.onemta.types.VehicleValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestSubwayRoute {

    private static MTA mta;

    private static Route route;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("subway");
        mta = TestProvider.getOneMTA();

        route = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE);
        VehicleValidation.requireVehicles(route);
    }

    @Nested
    final class ComparatorTests {

        @Test
        final void testNotExact(){
            assertFalse(route.isExactRoute(null));
            assertFalse(route.isExactRoute(999));
            assertFalse(route.isExactRoute("999"));

            assertFalse(route.isExactRoute(TestProvider.SUBWAY_ROUTE + 'x'));
            assertFalse(route.isExactRoute(TestProvider.SUBWAY_ROUTE + 'X'));
        }

        @Test
        final void testExact(){
            assertTrue(route.isExactRoute(route));
            assertTrue(route.isExactRoute(mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isExactRoute(Integer.valueOf(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isExactRoute(TestProvider.SUBWAY_ROUTE));
        }

        @Test
        final void testNotSame(){
            assertFalse(route.isSameRoute(null));
            assertFalse(route.isSameRoute(999));
            assertFalse(route.isSameRoute("999"));
        }

        @Test
        final void testSame(){
            assertTrue(route.isSameRoute(route));
            assertTrue(route.isSameRoute(mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isSameRoute(Integer.valueOf(TestProvider.SUBWAY_ROUTE)));
            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE));

            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE + 'x'));
            assertTrue(route.isSameRoute(TestProvider.SUBWAY_ROUTE + 'X'));
        }

    }

    @Nested
    final class ExtensionTests {

        @Test
        final void testStop(){
            SubwayExtensions.testRoute(route);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                for(final Vehicle vehicle : route.getVehicles())
                    SubwayExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                SubwayExtensions.testVehicleNumber(mta, route.getVehicles()[0]);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                for(final Vehicle vehicle : route.getVehicles()){
                    assertNotNull(vehicle.getTrip());
                    SubwayExtensions.testTrip(vehicle.getTrip());
                }
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                for(final Vehicle vehicle : route.getVehicles())
                    SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        final class AlertTests {

            @BeforeAll
            final void beforeAll(){
                AlertValidation.requireAlerts(route);
            }

            @Test
            final void testTransitAlerts(){
                { // missing description caused by MTA missing data
                    assertTrue(TestProvider.atleastOneTrue(
                        route.getAlerts(), Subway.Alert.class,
                        a -> a.getDescription() != null
                    ));
                }

                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                for(final Alert alert : route.getAlerts())
                    AlertValidation.testAlertReference(route, alert);
            }

        }

    }

}
