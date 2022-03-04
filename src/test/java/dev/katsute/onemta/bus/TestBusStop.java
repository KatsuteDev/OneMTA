package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBusStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();

        annotateTest(() -> stop = mta.getBusStop(TestProvider.BUS_STOP));
        annotateTest(() -> VehicleValidation.requireVehicles(stop));
    }

    @Nested
    final class TestExtensions {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // not all bus vehicles have this for some reason
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        stop.getVehicles(), Bus.Vehicle.class,
                        v -> v.getExpectedArrivalTime() != null &&
                            v.getExpectedArrivalTimeEpochMillis() != null &&
                            v.getExpectedDepartureTime() != null &&
                            v.getExpectedDepartureTimeEpochMillis() != null
                    )));
                }

                { // not all noProgress have a progress status for some reason
                    annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                        stop.getVehicles(), Bus.Vehicle.class,
                        v -> v.getProgressRate().equals("noProgress") &&
                            v.getProgressStatus() != null
                    )));
                }

                for(final Vehicle vehicle : stop.getVehicles())
                    BusExtensions.testVehicle(vehicle);
            }

            @Test
            final void testOrigin(){
                BusExtensions.testOriginStop(stop.getVehicles()[0]);
            }

            @Test
            final void testID(){
                BusExtensions.testVehicleNumber(mta, stop.getVehicles()[0]);
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

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                annotateTest(() -> assertTrue(TestProvider.atleastOneTrue(
                    stop.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            TripValidation.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                )));
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
                        stop.getAlerts(), Bus.Alert.class,
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
