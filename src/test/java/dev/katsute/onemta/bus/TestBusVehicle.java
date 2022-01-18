package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.*;

import static dev.katsute.jcore.Workflow.*;
import static dev.katsute.onemta.bus.Bus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

final class TestBusVehicle {

    private static MTA mta;

    private static Stop stop;
    private static Vehicle[] stopVehicles;
    private static Route route;
    private static Vehicle[] routeVehicles;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> {
            route = mta.getBusRoute(TestProvider.BUS_ROUTE);
            assumeTrue(route.getVehicles().length > 0, "There were no vehicles for route " + TestProvider.BUS_ROUTE + ", all vehicle tests were skipped");
            routeVehicles = route.getVehicles();

            stop = mta.getBusStop(TestProvider.BUS_STOP);
            assumeTrue(stop.getVehicles().length > 0, "There were no vehicles for stop " + TestProvider.BUS_STOP + ", all vehicle tests were skipped");
            stopVehicles = stop.getVehicles();
        });
    }

    @Nested
    final class TestTrip {

        @Test
        final void testTripStop(){
            annotateTest(() -> {
                for(final Vehicle vehicle : routeVehicles){
                    final TripStop[] stops = vehicle.getTrip().getTripStops();
                    assertNotEquals(0, stops.length);

                    boolean hasArrival = false;
                    for(final TripStop stop : stops){
                        hasArrival = hasArrival || (stop.getExpectedArrivalTime() != null && stop.getExpectedArrivalTimeEpochMillis() != null);

                        assertNotNull(stop.getArrivalProximityText());
                        assertNotNull(stop.getDistanceFromStop());
                        assertNotNull(stop.getStopsAway());
                        assertNotNull(stop.getStopName());
                        assertNotNull(stop.getStopID());
                    }
                    assertTrue(hasArrival);
                }
            });
        }

        @Test
        final void testTripStopStop(){
            annotateTest(() -> assertEquals(routeVehicles[0].getStopID(), routeVehicles[0].getTrip().getTripStops()[0].getStopID()));
        }

    }

    @Nested
    final class TestRoute {

        @Test
        final void testRoute(){
            annotateTest(() -> {
                for(final Vehicle vehicle : routeVehicles){
                    assertEquals(TestProvider.BUS_ROUTE, vehicle.getRouteID());
                    assertSame(route, vehicle.getRoute());
                }
            });
        }

    }

    // todo: vehicle stop is incorrect, stop always refers to the stop that requested vehicles, not what stop the vehicle is at
    @Nested
    final class TestStop {

        @Test
        final void testVehicle(){
            annotateTest(() -> {
                for(final Vehicle vehicle : stopVehicles){
                    assertSame(stop, vehicle.getStop());
                }
            });
        }

    }

}
