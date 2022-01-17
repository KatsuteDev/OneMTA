package dev.katsute.onemta.bus;

import dev.katsute.onemta.OneMTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestBusRoute {

    private static OneMTA MTA;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();
    }

    @Test
    public final void test(){

    }

}
