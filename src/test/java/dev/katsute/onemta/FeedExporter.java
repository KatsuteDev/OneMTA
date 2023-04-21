package dev.katsute.onemta;

import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SuppressWarnings("NewClassNamingConvention")
final class FeedExporter {

    private static MTAImpl mta;

    @BeforeAll
    static void beforeAll(){
        Assumptions.assumeFalse("true".equalsIgnoreCase(System.getenv("CI")));
        Assertions.assertTrue(new File("reference").exists() || new File("reference").mkdirs());

        mta = (MTAImpl) TestProvider.getOneMTA();
    }

    @Test
    final void exportBusFeeds() throws IOException{
        Files.write(
            new File("reference/bus-trip-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getTripUpdates()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/bus-position-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getVehiclePositions()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/bus-alerts-alt-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getAlerts()).getBytes(StandardCharsets.UTF_8)
        );
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    final void exportSubwayFeeds() throws IOException{
        Files.write(
            new File("reference/subway-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.get1234567()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-ace-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getACE()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-bdfm-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getBDFM()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-g-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getG()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-jz-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getJZ()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-l-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getL()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-nqrw-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getNQRW()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-si-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getSI()).getBytes(StandardCharsets.UTF_8)
        );
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    final void exportTrainFeeds() throws IOException{
        Files.write(
            new File("reference/lirr-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.lirr.getLIRR()).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/mnr-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.mnr.getMNR()).getBytes(StandardCharsets.UTF_8)
        );
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    final void exportAlertFeeds() throws IOException{
        Files.write(
            new File("reference/bus-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getBus()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getSubway()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/lirr-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getLIRR()).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/mnr-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getMNR()).getBytes(StandardCharsets.UTF_8)
        );
    }

}