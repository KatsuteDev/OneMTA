package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBusStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("bus");

        annotateTest(() -> stop = mta.getBusStop(TestProvider.BUS_STOP));
    }

    @Nested
    final class TestExtensions {

        @Test
        final void testRoute(){
            BusExtensions.testStop(stop);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));

                { // not all bus vehicles have this for some reason
                    boolean passes = false;
                    for(final Vehicle vehicle : stop.getVehicles()){
                        if(vehicle.getExpectedArrivalTime() != null &&
                           vehicle.getExpectedArrivalTimeEpochMillis() != null &&
                           vehicle.getExpectedDepartureTime() != null &&
                           vehicle.getExpectedDepartureTimeEpochMillis() != null
                        ){
                            passes = true;
                            break;
                        }
                    }

                    final boolean finalPasses = passes;
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests"));
                }

                { // not all noProgress have a progress status for some reason
                    boolean tested = false;
                    for(final Vehicle vehicle : stop.getVehicles()){
                        if(vehicle.getProgressRate().equals("noProgress") && vehicle.getProgressStatus() != null){
                            tested = true;
                            break;
                        }
                    }
                    final boolean finalPasses = tested;
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests"));
                }

                for(final Vehicle vehicle : stop.getVehicles())
                    BusExtensions.testVehicle(vehicle);
            }

            @Test
            final void testOrigin(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                BusExtensions.testOriginStop(stop.getVehicles()[0]);
            }

            @Test
            final void testID(){
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
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
                annotateTest(() -> VehicleValidation.requireVehicles(stop));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
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
