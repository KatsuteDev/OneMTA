package dev.katsute.onemta.subway;

import dev.katsute.onemta.MTA;
import dev.katsute.onemta.TestProvider;
import org.junit.jupiter.api.BeforeAll;

final class TestSubway {

    private static MTA mta;

    @BeforeAll
    static void beforeAll(){
        mta = TestProvider.getOneMTA();
    }

    // todo test routes from all feeds

    // todo: test route using int

    // todo: test stop using int

}
