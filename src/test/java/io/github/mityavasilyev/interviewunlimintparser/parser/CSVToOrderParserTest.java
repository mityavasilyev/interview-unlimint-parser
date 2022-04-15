package io.github.mityavasilyev.interviewunlimintparser.parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.github.mityavasilyev.interviewunlimintparser.extra.IdGenerator;
import io.github.mityavasilyev.interviewunlimintparser.model.Currency;
import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class CSVToOrderParserTest {

    @Mock
    CSVReader csvReader;

    @Mock
    IdGenerator idGenerator;

    @TempDir
    Path testDir;

    String[] dummyCSVArray = {"1", "24.5", "USD", "Random Purchase"};
    String dummyCSVLine = "1, 24.5, USD, Random Purchase";
    String dummyInvalidCSVLine = "1, 24.5, USD, Random Purchase, Extra";
    String dummyCorruptedCSVLine = "1, Hey, USD, Random Purchase";
    File testFile;

    CSVToOrderParserTest() throws AccessDeniedException, FileNotFoundException {
    }

    @Test
    void readNextOrder() throws CsvValidationException, IOException {

        testFile = new File(testDir.toFile(), "testfile.csv");
        List<String> csvLines = Arrays.asList(dummyCSVLine);

        Files.write(testFile.toPath(), csvLines);

        CSVToOrderParser parser = new CSVToOrderParser(testFile.toPath(), ',', idGenerator);
        Order order = parser.readNextOrder();

        assertEquals(Long.parseLong(dummyCSVArray[0]), order.getOrderId());
        assertEquals(Double.parseDouble(dummyCSVArray[1]), order.getAmount());
        assertEquals(dummyCSVArray[2], order.getCurrency().getShortName());
        assertEquals(dummyCSVArray[3], order.getComment());
        assertEquals("OK", order.getParsingResult());
    }

    @Test
    void readNextOrder_returnsDefaultOrderIfInvalidAmountOfArguments()
            throws CsvValidationException, IOException {

        testFile = new File(testDir.toFile(), "invalidtestfile.csv");
        List<String> csvLines = Arrays.asList(dummyInvalidCSVLine);

        Files.write(testFile.toPath(), csvLines);

        CSVToOrderParser parser = new CSVToOrderParser(testFile.toPath(), ',', idGenerator);
        Order order = parser.readNextOrder();

        assertEquals(-1L, order.getOrderId());
        assertEquals(-1D, order.getAmount());
        assertEquals(Currency.UNKNOWN.getShortName(), order.getCurrency().getShortName());
        assertEquals("", order.getComment());
        assertNotEquals("OK", order.getParsingResult());
    }

    @Test
    void readNextOrder_handlesInvalidProperties()
            throws CsvValidationException, IOException {

        testFile = new File(testDir.toFile(), "corruptedtestfile.csv");
        List<String> csvLines = Arrays.asList(dummyCorruptedCSVLine);

        Files.write(testFile.toPath(), csvLines);

        CSVToOrderParser parser = new CSVToOrderParser(testFile.toPath(), ',', idGenerator);
        Order order = parser.readNextOrder();

        assertEquals(Long.parseLong(dummyCSVArray[0]), order.getOrderId());
        assertEquals(0, order.getAmount());
        assertEquals(dummyCSVArray[2], order.getCurrency().getShortName());
        assertEquals(dummyCSVArray[3], order.getComment());
        assertNotEquals("OK", order.getParsingResult());
    }
}