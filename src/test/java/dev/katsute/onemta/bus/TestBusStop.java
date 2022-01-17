package dev.katsute.onemta.bus;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

final class TestBusStop {

    private static OneMTA MTA;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();
    }

}
