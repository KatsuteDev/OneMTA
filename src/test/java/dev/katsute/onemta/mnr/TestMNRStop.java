package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

import static dev.katsute.onemta.railroad.MNR.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNRStop {

    private static MTA mta;

    private static Stop stop;

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("mnr");
        mta = TestProvider.getOneMTA();

        stop = mta.getMNRStop(TestProvider.MNR_STOP);
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
            assertTrue(stop.isExactStop(mta.getMNRStop(TestProvider.MNR_STOP)));
            assertTrue(stop.isExactStop(TestProvider.MNR_STOP));
            assertTrue(stop.isExactStop(String.valueOf(TestProvider.MNR_STOP)));
        }

        @Test
        final void testStopCode(){
            assertTrue(stop.isExactStop(TestProvider.MNR_STOP_CODE));
            assertTrue(stop.isExactStop(TestProvider.MNR_STOP_CODE.toLowerCase()));
        }

    }

    @Nested
    final class ExtensionTests {

        @Nested
        final class VehicleTests {

            @Test
            final void testVehicles(){
                { // not all MNR vehicles have stops for some reason
                    assertTrue(TestProvider.atleastOneTrue(
                        stop.getVehicles(), MNR.Vehicle.class,
                        v -> v.getStopID() != null
                    ));
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

            @Test
            final void testVehicleTripsReference(){
                for(final Vehicle vehicle : stop.getVehicles())
                    TripValidation.testTripReference(vehicle);
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

    }

    @Nested
    final class AlertTests {

        @Test
        final void testTransitAlert(){
            AlertValidation.testAlerts(stop);
        }

    }

}
