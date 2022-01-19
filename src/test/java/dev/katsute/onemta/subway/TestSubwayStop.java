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

    @Nested
    final class DirectionTests {

        @Nested
        final class NorthTests {

            @Test
            final void testEnum(){
                 annotateTest(() -> Assertions.assertEquals(SubwayDirection.NORTH, stopN.getDirection()));
            }

            @Nested
            final class ExtensionTests {

                @Test
                final void testStop(){
                    SubwayExtensions.testStop(stopN);
                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            SubwayExtensions.testTrip(vehicle.getTrip());
                        }
                    }

                }

                @Nested
                final class TripStopTests {

                    @Test
                    final void testVehicleTripStops(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                }

            }

            @Nested
            final class InheritedTests {

                @Test
                final void testTransitStop(){
                    StopValidation.testStop(stopN);
                }

                @Nested
                final class VehicleTests {

                    @Test
                    final void testTransitVehicles(){
                        annotateTest(() -> Assumptions.assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                        for(final Vehicle vehicle : stopN.getVehicles())
                            VehicleValidation.testVehicle(vehicle);
                    }

                    @Test
                    final void testGTFSTransitVehicles(){
                        annotateTest(() -> Assumptions.assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                        for(final Vehicle vehicle : stopN.getVehicles())
                            VehicleValidation.testGTFSVehicle(vehicle);
                    }

                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testTrip(vehicle.getTrip());
                        }
                    }

                    @Test
                    final void testVehicleTripsReference(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles())
                            TripValidation.testTripReference(vehicle);
                    }

                    @Test
                    final void testGTFSVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testGTFSTrip(vehicle.getTrip());
                        }
                    }

                }

                @Nested
                final class TripStopTests {

                    @Test
                    final void testVehicleTripStops(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                    @Test
                    final void testGTFSTripStops(){
                        annotateTest(() -> assumeTrue(stopN.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                }

                @Nested
                final class AlertTests {

                    @Test
                    final void testTransitAlerts(){
                        annotateTest(() -> Assumptions.assumeTrue(stopN.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                        for(final Alert alert : stopN.getAlerts())
                            AlertValidation.testAlert(alert);
                    }

                    @Test
                    final void testTransitAlertsReference(){
                        annotateTest(() -> Assumptions.assumeTrue(stopN.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                        for(final Alert alert : stopN.getAlerts())
                            AlertValidation.testAlertReference(stopN, alert);
                    }

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
            final class ExtensionTests {

                @Test
                final void testStop(){
                    SubwayExtensions.testStop(stopS);
                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            SubwayExtensions.testTrip(vehicle.getTrip());
                        }
                    }

                }

                @Nested
                final class TripStopTests {

                    @Test
                    final void testVehicleTripStops(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                }

            }

            @Nested
            final class InheritedTests {

                @Test
                final void testTransitStop(){
                    StopValidation.testStop(stopS);
                }

                @Nested
                final class VehicleTests {

                    @Test
                    final void testTransitVehicles(){
                        annotateTest(() -> Assumptions.assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                        for(final Vehicle vehicle : stopS.getVehicles())
                            VehicleValidation.testVehicle(vehicle);
                    }

                    @Test
                    final void testGTFSTransitVehicles(){
                        annotateTest(() -> Assumptions.assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                        for(final Vehicle vehicle : stopS.getVehicles())
                            VehicleValidation.testGTFSVehicle(vehicle);
                    }

                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testTrip(vehicle.getTrip());
                        }
                    }

                    @Test
                    final void testVehicleTripsReference(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles())
                            TripValidation.testTripReference(vehicle);
                    }

                    @Test
                    final void testGTFSVehicleTrips(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testGTFSTrip(vehicle.getTrip());
                        }
                    }

                }

                @Nested
                final class TripStopTests {

                    @Test
                    final void testVehicleTripStops(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                    @Test
                    final void testGTFSTripStops(){
                        annotateTest(() -> assumeTrue(stopS.getVehicles().length > 0, "No vehicles found, skipping tests"));
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No vehicles found, skipping tests"));
                            TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                        }
                    }

                }

                @Nested
                final class AlertTests {

                    @Test
                    final void testTransitAlerts(){
                        annotateTest(() -> Assumptions.assumeTrue(stopS.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                        for(final Alert alert : stopS.getAlerts())
                            AlertValidation.testAlert(alert);
                    }

                    @Test
                    final void testTransitAlertsReference(){
                        annotateTest(() -> Assumptions.assumeTrue(stopS.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                        for(final Alert alert : stopN.getAlerts())
                            AlertValidation.testAlertReference(stopS, alert);
                    }

                }

            }

        }

    }

    @Nested
    final class ExtensionTests {

        @Test
        final void testStop(){
            SubwayExtensions.testStop(stop);
        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping tests"));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    SubwayExtensions.testTrip(vehicle.getTrip());
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
                    SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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
                annotateTest(() -> Assumptions.assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testGTFSTransitVehicles(){
                annotateTest(() -> Assumptions.assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testGTFSVehicle(vehicle);
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
