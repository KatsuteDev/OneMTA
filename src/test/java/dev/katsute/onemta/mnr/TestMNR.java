package dev.katsute.onemta.mnr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestMNR {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("mnr");
    }

    @Test
    final void testStopCode(){
        annotateTest(() -> assertEquals(TestProvider.MNR_STOP, mta.getMNRStop(TestProvider.MNR_STOP_CODE).getStopID()));
    }

}
