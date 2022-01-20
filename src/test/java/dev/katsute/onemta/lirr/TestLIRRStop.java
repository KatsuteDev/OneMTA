package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.LIRR.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SuppressWarnings("SpellCheckingInspection")
final class TestLIRRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("lirr");

        annotateTest(() -> stop = mta.getLIRRStop(TestProvider.LIRR_STOP));
    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles())
                    LIRRExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                LIRRExtensions.testVehicleNumber(mta, stop.getVehicles()[0]);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    LIRRExtensions.testTrip(vehicle.getTrip());
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

        @Test
        final void testRailroadStop(){
            StopValidation.testRailroadStop(stop);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testTransitVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
            }

            @Test
            final void testGTFSTransitVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testGTFSVehicle(vehicle);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assertNotNull(vehicle.getTrip()));
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

            @Test
            final void testVehicleTripsReference(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripReference(vehicle);
            }

            @Test
            final void testGTFSVehicleTrips(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
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
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
                }
            }

            @Test
            final void testGTFSTripStops(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    TripValidation.testGTFSTripStops(vehicle.getTrip().getTripStops());
                }
            }

            @Test
            final void testRailroadTripStops(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles()){
                    annotateTest(() -> assumeTrue(vehicle.getTrip().getTripStops().length > 0, "No trip stops found, skipping tests"));
                    TripValidation.testRailroadTripStops(vehicle.getTrip().getTripStops());
                }
            }

        }

        @Nested
        final class AlertTests {

            @Test
            final void testTransitAlerts(){
                annotateTest(() -> assumeTrue(stop.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlert(alert);
            }

            @Test
            final void testTransitAlertsReference(){
                annotateTest(() -> assumeTrue(stop.getAlerts().length > 0, "No alerts found, skipping alert tests"));
                for(final Alert alert : stop.getAlerts())
                    AlertValidation.testAlertReference(stop, alert);
            }

        }

    }

}
