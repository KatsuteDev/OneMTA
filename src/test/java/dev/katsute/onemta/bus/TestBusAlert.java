package dev.katsute.onemta.bus;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import dev.katsute.onemta.types.TestAlerts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestBusAlert {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    @Test
    final void testAlerts(){
        TestAlerts.testAlerts(mta.getBusAlerts());
    }

}
