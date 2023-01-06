package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.*;
import org.junit.jupiter.api.*;

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

        stop = mta.getLIRRStop(TestProvider.LIRR_STOP);
        VehicleValidation.requireVehicles(stop);
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
                    assertNotNull(vehicle.getTrip());
                    LIRRExtensions.testTrip(vehicle.getTrip());
                }
            }

        }

    }

    @Nested
    final class InheritedTests {

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

    @Test
    final void testStop(){
        assertNotNull(stop.getStopCode());
        assertNotNull(stop.getStopDescription());
        assertNotNull(stop.hasWheelchairBoarding());
    }

    @Nested
    final class StopTests {

        @Test
        final void testStop(){
            StopValidation.testStop(stop);
        }

        @Test
        final void testNotExact(){
            assertFalse(stop.isExactStop(null));
            assertFalse(stop.isExactStop(999));
            assertFalse(stop.isExactStop("999"));

            assertTrue(stop.isExactStop(TestProvider.LIRR_STOP_CODE));
            assertTrue(stop.isExactStop(TestProvider.LIRR_STOP_CODE.toLowerCase()));
        }

        @Test
        final void testExact(){
            assertTrue(stop.isExactStop(stop));
            assertTrue(stop.isExactStop(mta.getLIRRStop(TestProvider.LIRR_STOP)));
            assertTrue(stop.isExactStop(TestProvider.LIRR_STOP));
            assertTrue(stop.isExactStop(String.valueOf(TestProvider.LIRR_STOP)));
        }

        @Test
        final void testNotSame(){
            assertFalse(stop.isSameStop(null));
            assertFalse(stop.isSameStop(999));
            assertFalse(stop.isSameStop("999"));

            assertTrue(stop.isSameStop(TestProvider.LIRR_STOP_CODE));
            assertTrue(stop.isSameStop(TestProvider.LIRR_STOP_CODE.toLowerCase()));
        }

        @Test
        final void testSame(){
            assertTrue(stop.isSameStop(stop));
            assertTrue(stop.isSameStop(mta.getLIRRStop(TestProvider.LIRR_STOP)));
            assertTrue(stop.isSameStop(TestProvider.LIRR_STOP));
            assertTrue(stop.isSameStop(String.valueOf(TestProvider.LIRR_STOP)));
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
