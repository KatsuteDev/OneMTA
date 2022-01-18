package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayVehicle {

    private static MTA mta;

    private static Subway.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = mta.getSubwayRoute(TestProvider.SUBWAY_ROUTE).getVehicles());
    }

}
