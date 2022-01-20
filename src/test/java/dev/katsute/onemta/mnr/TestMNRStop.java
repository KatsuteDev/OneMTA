package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestMNRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("mnr");

        annotateTest(() -> stop = mta.getMNRStop(TestProvider.MNR_STOP));
    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));

                { // not all MNR vehicles have stops for some reason
                    boolean tested = false;
                    for(final Vehicle vehicle : stop.getVehicles()){
                        if(vehicle.getStopID() != null){
                            tested = true;
                            break;
                        }
                    }
                    final boolean finalTested = tested;
                    annotateTest(() -> assertTrue(finalTested, "Failed to pass stop id tests"));
                }

                for(final Vehicle vehicle : stop.getVehicles())
                    MNRExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                MNRExtensions.testVehicleNumber(mta, stop.getVehicles()[0]);
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
