package dev.katsute.onemta;

import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestWIP {

    @Test
    public void test() throws IOException{
        final OneMTAImpl mta = (OneMTAImpl) TestProvider.getOneMTA();

        Files.write(new File("reference/subway-gtfs.json").toPath(), JsonFormat.printer().print(mta.service.subway.get1234567(mta.subwayToken)).getBytes(StandardCharsets.UTF_8));
        Files.write(new File("reference/lirr-gtfs.json").toPath(), JsonFormat.printer().print(mta.service.lirr.getLIRR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8));
        Files.write(new File("reference/mnr-gtfs.json").toPath(), JsonFormat.printer().print(mta.service.mnrr.getMNRR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8));
    }

}
