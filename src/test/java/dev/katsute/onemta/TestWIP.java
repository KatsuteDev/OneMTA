package dev.katsute.onemta;

import org.junit.jupiter.api.Test;

public class TestWIP {

    @Test
    public void test(){
        final OneMTA mta = TestProvider.getOneMTA();

        //mta.getLIRR(null);
        mta.getMNRTrain(null);
    }

}
