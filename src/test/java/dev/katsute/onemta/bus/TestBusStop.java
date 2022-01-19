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
        mta = TestProvider.getOneMTA();

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
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));

                {
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
                    annotateTest(() -> assumeTrue(finalPasses, "Failed to pass expected arrival tests"));
                }

                boolean tested = false;
                for(final Vehicle vehicle : stop.getVehicles()){
                    if(!tested && vehicle.getProgressRate().equals("noProgress")){
                        annotateTest(() -> assertNotNull(vehicle.getProgressStatus()));
                        tested = true;
                    }
                    BusExtensions.testVehicle(vehicle);
                }
            }

            @Test
            final void testOrigin(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                BusExtensions.testOriginStop(stop.getVehicles()[0]);
            }

            @Test
            final void testID(){
                annotateTest(() -> assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
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
                annotateTest(() -> Assumptions.assumeTrue(stop.getVehicles().length > 0, "No vehicles found, skipping vehicle tests"));
                for(final Vehicle vehicle : stop.getVehicles())
                    VehicleValidation.testVehicle(vehicle);
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
