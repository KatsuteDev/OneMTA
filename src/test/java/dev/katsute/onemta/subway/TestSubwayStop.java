package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestSubwayStop {

    private static MTA mta;

    private static Stop stop;
    private static Stop stopN;
    private static Stop stopS;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getSubwayStop(TestProvider.SUBWAY_STOP));
        annotateTest(() -> stopN = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "N"));
        annotateTest(() -> stopS = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "S"));
    }

    @Test
    final void testMethods(){
        annotateTest(() -> Assertions.assertNull(stop.getDirection()));
    }

    @Nested
    final class DirectionTests {

        @Nested
        final class NorthTests {

            @Test
            final void testEnum(){
                 annotateTest(() -> Assertions.assertEquals(SubwayDirection.NORTH, stopN.getDirection()));
            }

            @Nested
            final class InheritedTests {

                @Test
                final void testTransitStop(){
                    StopValidation.testStop(stopN);
                }

                @Test
                final void testTransitVehicles(){
                    annotateTest(() -> Assertions.assertNotNull(stopN.getVehicles()));
                    VehicleValidation.testVehicles(stopN.getVehicles());
                }

                @Test
                final void testGTFSTransitVehicles(){
                    annotateTest(() -> Assertions.assertNotNull(stopN.getVehicles()));
                    VehicleValidation.testGTFSVehicles(stopN.getVehicles());
                }

                @Test
                final void testTransitAlerts(){
                    AlertValidation.testAlertsReference(stopN);
                }

            }

        }

        @Nested
        final class SouthTests {

            @Test
            final void testEnum(){
                 annotateTest(() -> Assertions.assertEquals(SubwayDirection.SOUTH, stopS.getDirection()));
            }

            @Nested
            final class InheritedTests {

                @Test
                final void testTransitStop(){
                    StopValidation.testStop(stopS);
                }

                @Test
                final void testTransitVehicles(){
                    annotateTest(() -> Assertions.assertNotNull(stopS.getVehicles()));
                    VehicleValidation.testVehicles(stopS.getVehicles());
                }

                @Test
                final void testGTFSTransitVehicles(){
                    annotateTest(() -> Assertions.assertNotNull(stopS.getVehicles()));
                    VehicleValidation.testGTFSVehicles(stopS.getVehicles());
                }

                @Test
                final void testTransitAlerts(){
                    AlertValidation.testAlertsReference(stopS);
                }

            }

        }

    }

    @Nested
    final class InheritedTests {

        @Test
        final void testTransitStop(){
            StopValidation.testStop(stop);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testTransitVehicles(){
                annotateTest(() -> Assertions.assertNotNull(stop.getVehicles()));
                VehicleValidation.testVehicles(stop.getVehicles());
            }

            @Test
            final void testGTFSTransitVehicles(){
                annotateTest(() -> Assertions.assertNotNull(stop.getVehicles()));
                VehicleValidation.testGTFSVehicles(stop.getVehicles());
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testGTFSVehicleTrips(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testGTFSTrip(vehicle.getTrip());
                }
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                }
            }

            @Test
            final void testGTFSTripStops(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                    TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                }
            }

        }

        @Nested
        final class AlertTests {

            @Test
            final void testTransitAlerts(){
                annotateTest(() -> Assumptions.assumeTrue(stop.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                annotateTest(() -> Assumptions.assumeTrue(stop.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlertReference(stop, alert);
            }

        }

    }

}
