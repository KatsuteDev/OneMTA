package dev.katsute.onemta;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestWIP {

    @Test
    public void test() throws IOException{
        final OneMTA mta = TestProvider.getOneMTA();

        Files.write(new File("test.json").toPath(),JsonFormat.printer().print((MessageOrBuilder) ((OneMTAImpl) mta).test()).getBytes(StandardCharsets.UTF_8));
    }

}
