package dev.katsute.onemta;

import com.google.gson.*;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SuppressWarnings("NewClassNamingConvention")
final class FeedExporter {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static MTAImpl mta;

    @BeforeAll
    static void beforeAll(){
        Assumptions.assumeFalse("true".equalsIgnoreCase(System.getenv("CI")));
        Assertions.assertTrue(new File("reference").exists() || new File("reference").mkdirs());

        mta = (MTAImpl) TestProvider.getOneMTA();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    final void exportFeeds() throws IOException{
        Files.write(
            new File("reference/bus-stop-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getStop(mta.busToken, 400561, null, null).getRaw())).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/bus-vehicle-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getVehicle(mta.busToken, null, "M1", null, null).getRaw())).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/subway-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.get1234567(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/lirr-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.lirr.getLIRR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/mnr-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.mnr.getMNR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    final void exportBusVehicle() throws IOException{
         final MTAImpl mta = (MTAImpl) TestProvider.getOneMTA();

         assert mta != null;

         final Gson gson = new GsonBuilder().setPrettyPrinting().create();

         Files.write(
            new File("reference/bus-vehicle-specific-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getVehicle(mta.busToken, 9567, null, null, null).getRaw())).getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    final void exportAllFeeds() throws IOException{
        Files.write(
            new File("reference/bus-trip-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getTripUpdates(mta.busToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/bus-position-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getVehiclePositions(mta.busToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/bus-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.bus.getAlerts(mta.busToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-ace-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getACE(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-bdfm-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getBDFM(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-g-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getG(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-jz-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getJZ(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-l-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getL(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-nqrw-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getNQRW(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            new File("reference/subway-si-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.subway.getSI(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    final void exportServiceAlerts() throws IOException{
        Files.write(
            new File("reference/bus-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getBus(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/subway-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getSubway(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/lirr-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getLIRR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/mnr-alerts-gtfs.json").toPath(),
            JsonFormat.printer().print(mta.service.alerts.getMNR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
    }

}
