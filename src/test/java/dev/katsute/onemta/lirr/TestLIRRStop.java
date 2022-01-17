package dev.katsute.onemta.lirr;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

final class TestLIRRStop {

    private static OneMTA MTA;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();
    }

}
