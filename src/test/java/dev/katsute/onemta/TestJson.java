package dev.katsute.onemta;

import dev.katsute.onemta.exception.JsonSyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static dev.katsute.onemta.Json.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
public class TestJson {

    private static JsonObject jsonObject;
    private static List<?> jsonArray;

    @BeforeAll
    static void beforeAll() throws IOException{
        final String map = TestProvider.readFile(new File("src/test/java/resources/map.json")).replaceAll("\\r?\\n", "");
        jsonObject = (JsonObject) parse(map);

        final String arr = TestProvider.readFile(new File("src/test/java/resources/arr.json")).replaceAll("\\r?\\n", "");
        jsonArray = (List<?>) parse(arr);
    }

    // map

    @ParameterizedTest(name="[{index}] {0}")
    @MethodSource("mapProvider")
    final void testMap(final Object expected, final Object actual){
        assertEquals(expected, actual);
    }

    @Test
    final void testMapNull(){
        assertNull(jsonObject.get("null"));
        assertTrue(jsonObject.containsKey("null"));
        assertNull(jsonObject.get("nulls"));
        assertTrue(jsonObject.containsKey("nulls"));
    }

    @Test
    final void testMapBoolean(){
        assertTrue(jsonObject.getBoolean("bool"));
        assertFalse(jsonObject.getBoolean("bools"));
    }

    @Test
    final void testMapMap(){
        assertEquals("v", jsonObject.getJsonObject("obj").getString("k"));
        assertEquals(0, jsonObject.getJsonObject("cobj").size());
    }

    @Test
    final void testMapArray(){
        assertEquals("str", jsonObject.getStringArray("arr")[0]);
        assertEquals(0, jsonObject.getJsonArray("carr").length);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> mapProvider(){
        return new TestProvider.ObjectStream()
            .add(1.0, jsonObject.getDouble("double"))
            .add(-1.0, jsonObject.getDouble("doublen"))
            .add(1.0, jsonObject.getDouble("doubles"))
            .add(1, jsonObject.getInt("int"))
            .add(-1, jsonObject.getInt("intn"))
            .add(1, jsonObject.getInt("ints"))
            .add("string", jsonObject.getString("string"))
            .add("string", jsonObject.getString("strings"))
            .add("str\"ing", jsonObject.getString("str\"ingx"))
            .add("/\\", jsonObject.getString("slash\\"))
            .add("何", jsonObject.getString("何"))
            .add("\\u4f55", jsonObject.getString("\\u4f55"))
            .stream();
    }

    // array

    @ParameterizedTest(name="[{index}] {0}")
    @MethodSource("arrayProvider")
    final void testArray(final Object object){
        assertTrue(jsonArray.contains(object));
    }

    @Test
    final void testArrayMap(){
        assertEquals("v", ((JsonObject) jsonArray.get(15)).getString("k"));
    }

    @Test
    final void testArrayEmptyMap(){
        assertEquals(0, ((JsonObject) jsonArray.get(16)).size());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> arrayProvider(){
        return new TestProvider.ObjectStream()
            .add(1.0)
            .add(-1.0)
            .add(2.0)
            .add(1)
            .add(-1)
            .add(2)
            .add(true)
            .add(false)
            .add((Object) null)
            .add("string")
            .add("str\"ingx")
            .add("/\\")
            .add("何")
            .add("\\u4f55")
            .add(new ArrayList<String>(){{ add("str"); }})
            .add(new ArrayList<String>())
            .stream();
    }

    // malformed

    @ParameterizedTest(name="[{index}] {0}")
    @ValueSource(strings={"", "?", "{", "}", "[", "]", "{{", "}}", "[[", "]]", "{[", "[{", "}]", "]}", "{[}]", "[{]}"})
    final void testMalformed(final String string){
        assertThrows(JsonSyntaxException.class, () -> parse(string));
    }

    // newline

    @Test
    final void testNewLine(){
        assertEquals("v", ((JsonObject) parse("{\"k\":\n\"v\"\n}")).getString("k"));
    }

}