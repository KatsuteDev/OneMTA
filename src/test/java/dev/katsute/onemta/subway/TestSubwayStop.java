package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.subway.Subway.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestSubwayStop {

    private static MTA mta;

    private static Stop stop;
    private static Stop stopN;
    private static Stop stopS;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("subway");
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getSubwayStop(TestProvider.SUBWAY_STOP));
        annotateTest(() -> stopN = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "N"));
        annotateTest(() -> stopS = mta.getSubwayStop(TestProvider.SUBWAY_STOP + "S"));

        annotateTest(() -> VehicleValidation.requireVehicles(stop));
        annotateTest(() -> VehicleValidation.requireVehicles(stopN));
        annotateTest(() -> VehicleValidation.requireVehicles(stopS));
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
                        for(final Vehicle vehicle : stopN.getVehicles())
                            SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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
                        for(final Vehicle vehicle : stopN.getVehicles())
                            VehicleValidation.testVehicle(vehicle);
                    }

                    @Test
                    final void testGTFSTransitVehicles(){
                        for(final Vehicle vehicle : stopN.getVehicles())
                            VehicleValidation.testGTFSVehicle(vehicle);
                    }

                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        for(final Vehicle vehicle : stopN.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testTrip(vehicle.getTrip());
                        }
                    }

                    @Test
                    final void testVehicleTripsReference(){
                        for(final Vehicle vehicle : stopN.getVehicles())
                            TripValidation.testTripReference(vehicle);
                    }

                    @Test
                    final void testGTFSVehicleTrips(){
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
                        for(final Vehicle vehicle : stopN.getVehicles())
                            TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                    }

                    @Test
                    final void testGTFSTripStops(){
                        for(final Vehicle vehicle : stopN.getVehicles())
                            TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                    }

                }

                @TestInstance(TestInstance.Lifecycle.PER_CLASS)
                @Nested
                final class AlertTests {

                    @BeforeAll
                    final void beforeAll(){
                        annotateTest(() -> AlertValidation.requireAlerts(stop));
                    }

                    @Test
                    final void testTransitAlerts(){
                        for(final Alert alert : stopN.getAlerts())
                            AlertValidation.testAlert(alert);
                    }

                    @Test
                    final void testTransitAlertsReference(){
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
                        for(final Vehicle vehicle : stopS.getVehicles())
                            SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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
                        for(final Vehicle vehicle : stopS.getVehicles())
                            VehicleValidation.testVehicle(vehicle);
                    }

                    @Test
                    final void testGTFSTransitVehicles(){
                        for(final Vehicle vehicle : stopS.getVehicles())
                            VehicleValidation.testGTFSVehicle(vehicle);
                    }

                }

                @Nested
                final class TripTests {

                    @Test
                    final void testVehicleTrips(){
                        for(final Vehicle vehicle : stopS.getVehicles()){
                            annotateTest(() -> assertNotNull(vehicle.getTrip()));
                            TripValidation.testTrip(vehicle.getTrip());
                        }
                    }

                    @Test
                    final void testVehicleTripsReference(){
                        for(final Vehicle vehicle : stopS.getVehicles())
                            TripValidation.testTripReference(vehicle);
                    }

                    @Test
                    final void testGTFSVehicleTrips(){
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
                        for(final Vehicle vehicle : stopS.getVehicles())
                            TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                    }

                    @Test
                    final void testGTFSTripStops(){
                        for(final Vehicle vehicle : stopS.getVehicles())
                            TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                    }

                }

                @TestInstance(TestInstance.Lifecycle.PER_CLASS)
                @Nested
                final class AlertTests {

                    @BeforeAll
                    final void beforeAll(){
                        annotateTest(() -> AlertValidation.requireAlerts(stop));
                    }

                    @Test
                    final void testTransitAlerts(){
                        for(final Alert alert : stopS.getAlerts())
                            AlertValidation.testAlert(alert);
                    }

                    @Test
                    final void testTransitAlertsReference(){
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
        final class VehicleTests {

            @Test
            final void testID(){
                SubwayExtensions.testVehicleNumber(mta, stop.getVehicles()[0]);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
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
                for(final Vehicle vehicle : stop.getVehicles())
                    SubwayExtensions.testTripStops(vehicle.getTrip().getTripStops());
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
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testGTFSTransitVehicles(){
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testGTFSVehicle(vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testGTFSVehicleTrips(){
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
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
            }

            @Test
            final void testGTFSTripStops(){
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
            }

        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        final class AlertTests {

            @BeforeAll
            final void beforeAll(){
                annotateTest(() -> AlertValidation.requireAlerts(stop));
            }

            @Test
            final void testTransitAlerts(){
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlertReference(stop, alert);
            }

        }

    }

}
