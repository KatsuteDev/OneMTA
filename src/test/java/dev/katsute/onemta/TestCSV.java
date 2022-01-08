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
        public final void testHeader(){
            final CSV csv = new CSV("header,header2");
            assertEquals(
                Arrays.asList("header", "header2"), csv.getHeaders(),
                errorSupplier("Expected header to match")
            );
        }

        @Test
        public final void testHeaderQuote(){
            final CSV csv = new CSV("header,\"header,2\"");
            assertEquals(
                Arrays.asList("header", "header,2"), csv.getHeaders(),
                errorSupplier("Expected header to match")
            );
        }

    }

    @Nested
    final class TestRow {

        @Test
        public final void testRow(){
            final CSV csv = new CSV("key,value\n1,one\n2,two");
            assertEquals(
                Arrays.asList("1", "one"), csv.getRow("key", "1"),
                errorSupplier("Expected row to match")
            );
            assertEquals(
                Arrays.asList("2", "two"), csv.getRow("key", "2"),
                errorSupplier("Expected row to match")
            );
        }

        @Test
        public final void testRowQuote(){
            final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");
            assertEquals(
                Arrays.asList("1,", "one,"), csv.getRow("key", "1,"),
                errorSupplier("Expected row to match")
            );
            assertEquals(
                Arrays.asList(",2", ",two"), csv.getRow("key", ",2"),
                errorSupplier("Expected row to match")
            );
        }

        @Test
        public final void testNull(){
            final CSV csv = new CSV("key,value");
            assertNull(
                csv.getRow("key", "1"),
                errorSupplier("Expected row to be null")
            );
        }

    }

    @Nested
    final class TestRowValue {

        @Test
        public final void testValue(){
            final CSV csv = new CSV("key,value\n1,one\n2,two");
            assertEquals(
                "one", csv.getValue("key", "1", "value"),
                errorSupplier("Expected value to match")
            );
            assertEquals(
                "two", csv.getValue("key", "2", "value"),
                errorSupplier("Expected value to match")
            );
        }

        @Test
        public final void testValueQuote(){
            final CSV csv = new CSV("key,value\n\"1,\",\"one,\"\n\",2\",\",two\"");
            assertEquals(
                "one,", csv.getValue("key", "1,", "value"),
                errorSupplier("Expected row to match")
            );
            assertEquals(
                ",two", csv.getValue("key", ",2", "value"),
                errorSupplier("Expected row to match")
            );
        }

        @Test
        public final void testNull(){
            final CSV csv = new CSV("key,value\n1,one");
            assertNull(
                csv.getValue("key", "1", "null"),
                errorSupplier("Expected value to be null")
            );
            assertNull(
                csv.getValue("key", "2", "null"),
                errorSupplier("Expected value to be null")
            );
        }

    }

}
