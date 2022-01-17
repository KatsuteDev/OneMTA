package dev.katsute.onemta;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class MTATests {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void test(){

    }

}
