package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestBusStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("bus");
        mta = TestProvider.getOneMTA();

        stop = mta.getBusStop(TestProvider.BUS_STOP);
        VehicleValidation.requireVehicles(stop);
    }

    @Nested
    final class ComparatorTests {

        @Test
        final void testNotExact(){
            assertFalse(stop.isExactStop(null));
            assertFalse(stop.isExactStop(999));
            assertFalse(stop.isExactStop("999"));
        }

        @Test
        final void testExact(){
            assertTrue(stop.isExactStop(stop));
            assertTrue(stop.isExactStop(mta.getBusStop(TestProvider.BUS_STOP)));
            assertTrue(stop.isExactStop(TestProvider.BUS_STOP));
            assertTrue(stop.isExactStop(String.valueOf(TestProvider.BUS_STOP)));
        }

    }

    @Nested
    final class TestExtensions {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // test trip refresh
                    assertTrue(TestProvider.atleastOneTrue(
                        stop.getVehicles(), Bus.Vehicle.class,
                        v -> {
                            final Bus.Vehicle temp = mta.getBus(v.getVehicleID());
                            final Bus.Trip trip = temp.getTrip();
                            temp.refresh();
                            return trip != temp.getTrip();
                        }
                    ));
                }

                for(final Vehicle vehicle : stop.getVehicles())
                    BusExtensions.testVehicle(vehicle);
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
                    assertNotNull(vehicle.getTrip());
                    TripValidation.testTrip(vehicle.getTrip());
                }
            }

        }

        @Nested
        final class TripStopTests {

            @Test
            final void testVehicleTripStops(){
                assertTrue(TestProvider.atleastOneTrue(
                    stop.getVehicles(), Bus.Vehicle.class,
                    v -> {
                        if(v.getTrip() != null && v.getTrip().getTripStops().length > 0){
                            TripValidation.testTripStops(v.getTrip().getTripStops());
                            return true;
                        }
                        return false;
                    }
                ));
            }

        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        final class AlertTests {

            @BeforeAll
            final void beforeAll(){
                AlertValidation.requireAlerts(stop);
            }

            @Test
            final void testTransitAlerts(){
                { // missing description caused by MTA missing data
                    assertTrue(TestProvider.atleastOneTrue(
                        stop.getAlerts(), Bus.Alert.class,
                        a -> a.getDescription() != null
                    ));
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
