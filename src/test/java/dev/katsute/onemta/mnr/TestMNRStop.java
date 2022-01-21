package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getMNRStop(TestProvider.MNR_STOP));
    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @BeforeEach
            final void beforeEach(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
            }

            @Test
            final void testVehicles(){
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

            @BeforeEach
            final void beforeEach(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
            }

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

            @BeforeEach
            final void beforeEach(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
            }

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

            @BeforeEach
            final void beforeEach(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
            }

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

            @Test
            final void testRailroadTripStops(){
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testRailroadTripStops(vehicle.getTrip().getTripStops());
            }

        }

        @Nested
        final class AlertTests {

            @BeforeEach
            final void beforeEach(){
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
