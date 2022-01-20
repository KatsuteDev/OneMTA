package dev.katsute.onemta.lirr;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestLIRR {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA("lirr");
    }

    @Test
    final void testStopCode(){
        annotateTest(() -> assertEquals(TestProvider.LIRR_STOP, mta.getLIRRStop(TestProvider.LIRR_STOP_CODE).getStopID()));
    }

}
