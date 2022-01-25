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

        @Test
        final void testRoute(){
            BusExtensions.testStop(stop);
        }

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
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
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests, there probably wasn't enough vehicles to conclude test (tested " + stop.getVehicles().length + " vehicles)"));
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
                    annotateTest(() -> assertTrue(finalPasses, "Failed to pass expected arrival tests, there probably wasn't enough vehicles to conclude test (tested " + stop.getVehicles().length + " vehicles)"));
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
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripStops(vehicle.getTrip().getTripStops());
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
