package dev.katsute.onemta;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class MTATests {

    private static OneMTA MTA;

    @BeforeAll
    static void beforeAll(){
        MTA = TestProvider.getOneMTA();
    }

    @Test
    final void test(){

    }

}
