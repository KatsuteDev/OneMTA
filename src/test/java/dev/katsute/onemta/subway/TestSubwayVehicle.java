package dev.katsute.onemta.subway;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

import static dev.katsute.jcore.Workflow.*;

final class TestSubwayVehicle {

    private static OneMTA MTA;

    private static Subway.Vehicle[] vehicles;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();

        annotateTest(() -> vehicles = MTA.getSubwayRoute(TestProvider.SUBWAY_ROUTE).getVehicles());
    }

}
