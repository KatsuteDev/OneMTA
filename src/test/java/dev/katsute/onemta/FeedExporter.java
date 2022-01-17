package dev.katsute.onemta;

import com.google.gson.*;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SuppressWarnings("NewClassNamingConvention")
public class FeedExporter {

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void exportFeeds() throws IOException{
        final MTAImpl mta = (MTAImpl) TestProvider.getOneMTA();

        assert mta != null;

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Files.write(
            new File("reference/bus-stop-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getStop(mta.busToken, 400561, null, null).getRaw())).getBytes(StandardCharsets.UTF_8)
        );

        Files.write(
            new File("reference/bus-vehicle-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getVehicle(mta.busToken, null, "M1", null).getRaw())).getBytes(StandardCharsets.UTF_8)
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
            JsonFormat.printer().print(mta.service.mnrr.getMNRR(mta.subwayToken)).getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    public void exportBusVehicle() throws IOException{
         final MTAImpl mta = (MTAImpl) TestProvider.getOneMTA();

         assert mta != null;

         final Gson gson = new GsonBuilder().setPrettyPrinting().create();

         Files.write(
            new File("reference/bus-vehicle-specific-siri.json").toPath(),
            gson.toJson(JsonParser.parseString(mta.service.bus.getVehicle(mta.busToken, 3838, null, null).getRaw())).getBytes(StandardCharsets.UTF_8)
        );
    }

}
