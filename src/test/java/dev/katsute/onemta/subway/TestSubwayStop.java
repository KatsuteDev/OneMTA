package dev.katsute.onemta.subway;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

final class TestSubwayStop {

    private static OneMTA MTA;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();
    }

}
