package io.github.mityavasilyev.interviewunlimintparser.parser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import io.github.mityavasilyev.interviewunlimintparser.extra.IdGenerator;
import io.github.mityavasilyev.interviewunlimintparser.model.Currency;
import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UNKNOWN;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class CSVToOrderParser implements FileToOrdersParser {

    private final Path path;
    private final char delimiter;
    private final CSVReader csvReader;
    private final IdGenerator idGenerator;

    public CSVToOrderParser(Path path, IdGenerator idGenerator)
            throws FileNotFoundException, AccessDeniedException {

        this(path, ',', idGenerator);

    }

    public CSVToOrderParser(Path path, char delimiter, IdGenerator idGenerator)
            throws FileNotFoundException, AccessDeniedException {

        this.path = path;
        this.delimiter = delimiter;
        this.idGenerator = idGenerator;

        if (!Files.exists(path))
            throw new FileNotFoundException(String.format("No such file: %s", path.toAbsolutePath()));

        if (!Files.isReadable(path))
            throw new AccessDeniedException(String.format("Can't access file: %s", path.toAbsolutePath()));

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(delimiter)
                .build();

        csvReader = new CSVReaderBuilder(new FileReader(path.toAbsolutePath().toString()))
                .withCSVParser(csvParser)
                .build();
    }

    @Override
    public Order readNextOrder() {
        try {
            String[] values = csvReader.readNext();
            if (values != null) {
                return parseOrder(csvReader.getLinesRead(), values);
            }

            // From here it is the end of the file
            csvReader.close();
            return null;
        } catch (IllegalArgumentException exception) {
            return Order.builder()
                    .id(idGenerator.getNewId())
                    .orderId(-1L)
                    .amount(-1D)
                    .currency(Currency.UNKNOWN)
                    .comment("")
                    .fileName(this.path.getFileName().toString())
                    .line(csvReader.getLinesRead())
                    .parsingResult(exception.getMessage())
                    .build();
        } catch (IOException | CsvValidationException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    /**
     * Parses provided array of strings into an Order entity.
     * Throws IllegalArgumentException if provided array is not 4 objects long.
     *
     * @param line   Line of the document that corresponds to the provided values
     * @param values Array of strings that need to be parsed into order values
     * @return parsed Order entity with OK parsing result. Else returns default entity with error messages
     */
    private Order parseOrder(Long line, String[] values) {
        if (values.length != 4) throw new IllegalArgumentException("Invalid amount of values was provided");

        List<String> errors = new ArrayList<>();

        // Processing order ID
        long orderId;
        try {
            orderId = Optional
                    .of(Long.parseLong(values[0]))
                    .orElseThrow(() -> new IllegalArgumentException("No orderId provided"));
        } catch (Exception e) {
            log.warn("Failed to convert orderId: {}", values[0]);
            errors.add(String.format("Unable to convert ID: %s", values[0]));
            orderId = -1L;
        }

        // Processing amount
        double amount;
        try {
            amount = OptionalDouble
                    .of(Double.parseDouble(values[1]))
                    .orElseThrow(() -> new IllegalArgumentException("No amount provided"));
        } catch (Exception e) {
            log.warn("Failed to convert currency: {}", values[1]);
            errors.add(String.format("Unable to convert amount: %s", values[1]));
            amount = 0D;
        }

        // Processing currency
        Currency currency;
        try {
            currency = Optional
                    .of(Currency.valueOf(values[2].toUpperCase(Locale.ROOT)))
                    .orElseThrow(() -> new IllegalArgumentException("No currency provided"));

        } catch (Exception e) {
            log.warn("Failed to convert currency: {}", values[2]);
            errors.add(String.format("Unable to convert currency: %s", values[2]));
            currency = Currency.UNKNOWN;
        }

        // Processing comment
        String comment = values[3];

        return Order.builder()
                .id(idGenerator.getNewId())
                .orderId(orderId)
                .amount(amount)
                .currency(currency)
                .comment(comment)
                .fileName(this.path.getFileName().toString())
                .line(line)
                .parsingResult(OrderUtils.buildParseResult(errors))
                .build();
    }
}
