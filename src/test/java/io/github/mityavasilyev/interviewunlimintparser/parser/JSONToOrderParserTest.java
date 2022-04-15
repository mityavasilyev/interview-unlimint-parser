package io.github.mityavasilyev.interviewunlimintparser.parser;

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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JSONToOrderParserTest {

    @Mock
    IdGenerator idGenerator;

    @TempDir
    Path testDir;


    String dummyJSONLine = "[\n" +
            "    {\n" +
            "        \"orderId\": 3,\n" +
            "        \"amount\": 1.23,\n" +
            "        \"currency\": \"USD\",\n" +
            "        \"comment\": \"Random purchase\"\n" +
            "    }" +
            "]";

    String dummyCorruptedJSONLine = "[\n" +
            "    {\n" +
            "        \"orderId\": 3,\n" +
            "        \"amount\": \"Hey\",\n" +
            "        \"currency\": \"USD\",\n" +
            "        \"comment\": \"Random purchase\"\n" +
            "    }" +
            "]";

    File testFile;


    @Test
    void readNextOrder() throws CsvValidationException, IOException {

        testFile = new File(testDir.toFile(), "testfile.json");
        List<String> jsonLines = Arrays.asList(dummyJSONLine);

        Files.write(testFile.toPath(), jsonLines);

        JSONToOrderParser parser = new JSONToOrderParser(testFile.toPath(), idGenerator);

        when(idGenerator.getNewId())
                .thenReturn(1L);

        Order order = parser.readNextOrder();

        assertEquals(3L, order.getOrderId());
        assertEquals(1.23D, order.getAmount());
        assertEquals(Currency.USD.getShortName(), order.getCurrency().getShortName());
        assertEquals("Random purchase", order.getComment());
        assertEquals("OK", order.getParsingResult());
    }

    @Test
    void readNextOrder_handlesInvalidProperties()
            throws CsvValidationException, IOException {

        testFile = new File(testDir.toFile(), "corruptedtestfile.csv");
        List<String> csvLines = Arrays.asList(dummyCorruptedJSONLine);

        Files.write(testFile.toPath(), csvLines);

        JSONToOrderParser parser = new JSONToOrderParser(testFile.toPath(), idGenerator);
        Order order = parser.readNextOrder();

        assertEquals(3L, order.getOrderId());
        assertEquals(0, order.getAmount());
        assertEquals(Currency.USD.getShortName(), order.getCurrency().getShortName());
        assertEquals("Random purchase", order.getComment());
        assertNotEquals("OK", order.getParsingResult());
    }

}