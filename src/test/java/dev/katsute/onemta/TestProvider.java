package dev.katsute.onemta;

import dev.katsute.jcore.Workflow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.provider.Arguments;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class TestProvider {

    private static File bus = new File("src/test/java/resources/bus.txt");

    public static OneMTA getOneMTA(){
        try{
            return OneMTA.create(strip(readFile(bus)), null);
        }catch(final IOException e){
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Assertions.fail(Workflow.errorSupplier(sw.toString()));
            return null;
        }
    }

    // java 9

    public static String readFile(final File file) throws IOException{
        final StringBuilder OUT = new StringBuilder();
        for(final String s : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8))
            OUT.append(s);
        return OUT.toString();
    }

    // ^(\s+)|(\s+)$
    private static final Pattern dangling = Pattern.compile("^(\\s+)|(\\s+)$");

    static String strip(final String s){
        return dangling.matcher(s).replaceAll("");
    }

    //

    public static final class MethodStream<T> {

        private final List<Arguments> args = new ArrayList<>();

        public final MethodStream<T> add(final Function<T,Object> function){
            args.add(Arguments.of(function));
            return this;
        }

        public final MethodStream<T> add(final String method, final Function<T,Object> function){
            args.add(Arguments.of(method, function));
            return this;
        }

        public final Stream<Arguments> stream(){
            return args.stream();
        }

    }

    public static final class ObjectStream {

        private final List<Arguments> args = new ArrayList<>();

        public final ObjectStream add(final Object... object){
            args.add(Arguments.of(object));
            return this;
        }

        public final Stream<Arguments> stream(){
            return args.stream();
        }

    }

}