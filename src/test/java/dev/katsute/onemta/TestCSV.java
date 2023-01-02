package dev.katsute.onemta;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

final class TestCSV {

    @BeforeAll
    static void beforeAll(){
        TestProvider.testGroup("MTA");
    }

    @Nested
    final class TestHeader {

        @Test
        final void testHeader(){
            final CSV csv = new CSV("header,header2");

            assertEquals(Arrays.asList("header", "header2"), csv.getHeaders());
        }

        @Test
        final void testHeaderQuote(){
            final CSV csv = new CSV("header,\"header,2\"");

            assertEquals(Arrays.asList("header", "header,2"), csv.getHeaders());
        }

    }

    @Nested
    final class TestRow {

        @Test
        final void testRow(){
            final CSV csv = new CSV("key,value\n1,one\n2,two");

            assertEquals(Arrays.asList("1", "one"), csv.getRow("key", "1"));
            assertEquals(Arrays.asList("2", "two"), csv.getRow("key", "2"));
        }

        @Test
        final void testRowQuote(){
            final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");

            assertEquals(Arrays.asList("1,", "one,"), csv.getRow("key", "1,"));
            assertEquals(Arrays.asList(",2", ",two"), csv.getRow("key", ",2"));
        }

        @Test
        final void testNull(){
            final CSV csv = new CSV("key,value");

            assertNull(csv.getRow("key", "1"));
        }

        @Test
        final void testBlank(){
            final CSV csv = new CSV("key,value,blank\n1,one,\"\"\n2,two,\"\"");

            assertEquals(Arrays.asList("1", "one", ""), csv.getRow("key", "1"));
            assertEquals(Arrays.asList("2", "two", ""), csv.getRow("key", "2"));
        }

    }

    @Nested
    final class TestRowValue {

        @Test
        final void testValue(){
            final CSV csv = new CSV("key,value,key2\n1,one,1\n2,two,2");

            assertEquals("one", csv.getValue("key", "1", "value"));
            assertEquals("one", csv.getValue("key2", "1", "value"));
            assertEquals("two", csv.getValue("key", "2", "value"));
            assertEquals("two", csv.getValue("key2", "2", "value"));
        }

        @Test
        final void testValueMultiple(){
            final CSV csv = new CSV("key,value,key2\n1,one,1\n1,1,1\n2,two,2");

            assertArrayEquals(new String[]{"one", "1"}, csv.getValues("key", "1", "value"));
            assertArrayEquals(new String[]{"one", "1"}, csv.getValues("key2", "1", "value"));
            assertArrayEquals(new String[]{"two"}, csv.getValues("key", "2", "value"));
            assertArrayEquals(new String[]{"two"}, csv.getValues("key2", "2", "value"));
        }

        @Test
        final void testValueQuote(){
            final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");

            assertEquals("one,", csv.getValue("key", "1,", "value"));
            assertEquals(",two", csv.getValue("key", ",2", "value"));
        }

        @Test
        final void testNull(){
            final CSV csv = new CSV("key,value\n1,one");

            assertNull(csv.getValue("key", "1", "null"));
            assertNull(csv.getValue("key", "2", "null"));
        }

    }

}
