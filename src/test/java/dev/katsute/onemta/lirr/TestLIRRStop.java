package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.railroad.LIRR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestLIRRStop {

    private static MTA mta;

    private static Stop stop;

    @SuppressWarnings("SpellCheckingInspection")
    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("lirr");
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getLIRRStop(TestProvider.LIRR_STOP));
        annotateTest(() -> VehicleValidation.requireVehicles(stop));
    }

    @Nested
    final class ComparatorTests {

        @Test
        final void testNotExact(){
            annotateTest(() -> assertFalse(stop.isExactStop(null)));
            annotateTest(() -> assertFalse(stop.isExactStop(999)));
            annotateTest(() -> assertFalse(stop.isExactStop("999")));
        }

        @Test
        final void testExact(){
            annotateTest(() -> assertTrue(stop.isExactStop(stop)));
            annotateTest(() -> assertTrue(stop.isExactStop(mta.getLIRRStop(TestProvider.LIRR_STOP))));
            annotateTest(() -> assertTrue(stop.isExactStop(TestProvider.LIRR_STOP)));
            annotateTest(() -> assertTrue(stop.isExactStop(String.valueOf(TestProvider.LIRR_STOP))));
        }

        @Test
        final void testStopCode(){
            annotateTest(() -> assertTrue(stop.isExactStop(TestProvider.LIRR_STOP_CODE)));
            annotateTest(() -> assertTrue(stop.isExactStop(TestProvider.LIRR_STOP_CODE.toLowerCase())));
        }

    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                for(final Vehicle vehicle : stop.getVehicles())
                    LIRRExtensions.testVehicle(vehicle);
            }

            @Test
            final void testID(){
                LIRRExtensions.testVehicleNumber(mta, stop.getVehicles()[0]);
            }

        }

        @Nested
        final class TripTests {

            @Test
            final void testVehicleTrips(){
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

            @Test
            final void testRailroadTripStops(){
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testRailroadTripStops(vehicle.getTrip().getTripStops());
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
                { // missing description caused by MTA missing data
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        stop.getAlerts(), LIRR.Alert.class,
                        a -> a.getDescription() != null
                    )));
                }

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
