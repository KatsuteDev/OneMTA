package dev.katsute.onemta;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static dev.katsute.jcore.Workflow.*;
import static org.junit.jupiter.api.Assertions.*;

final class TestCSV {

    @Nested
    final class TestHeader {

        @Test
        final void testHeader(){
            annotateTest(() -> {
                final CSV csv = new CSV("header,header2");

                assertEquals(Arrays.asList("header", "header2"), csv.getHeaders());
            });

        }

        @Test
        final void testHeaderQuote(){
            annotateTest(() -> {
                final CSV csv = new CSV("header,\"header,2\"");

                assertEquals(Arrays.asList("header", "header,2"), csv.getHeaders());
            });
        }

    }

    @Nested
    final class TestRow {

        @Test
        final void testRow(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value\n1,one\n2,two");

                assertEquals(Arrays.asList("1", "one"), csv.getRow("key", "1"));
                assertEquals(Arrays.asList("2", "two"), csv.getRow("key", "2"));
            });
        }

        @Test
        final void testRowQuote(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");

                assertEquals(Arrays.asList("1,", "one,"), csv.getRow("key", "1,"));
                assertEquals(Arrays.asList(",2", ",two"), csv.getRow("key", ",2"));
            });

        }

        @Test
        final void testNull(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value");

                assertNull(csv.getRow("key", "1"));
            });
        }

        @Test
        final void testBlank(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value,blank\n1,one,\"\"\n2,two,\"\"");

                assertEquals(Arrays.asList("1", "one", ""), csv.getRow("key", "1"));
                assertEquals(Arrays.asList("2", "two", ""), csv.getRow("key", "2"));
            });
        }

    }

    @Nested
    final class TestRowValue {

        @Test
        final void testValue(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value\n1,one\n2,two");

                assertEquals("one", csv.getValue("key", "1", "value"));
                assertEquals("two", csv.getValue("key", "2", "value"));
            });

        }

        @Test
        final void testValueQuote(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");

                assertEquals("one,", csv.getValue("key", "1,", "value"));
                assertEquals(",two", csv.getValue("key", ",2", "value"));
            });
        }

        @Test
        final void testNull(){
            annotateTest(() -> {
                final CSV csv = new CSV("key,value\n1,one");

                assertNull(csv.getValue("key", "1", "null"));
                assertNull(csv.getValue("key", "2", "null"));
            });
        }

    }

}
