package io.github.mityavasilyev.interviewunlimintparser.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mityavasilyev.interviewunlimintparser.extra.IdGenerator;
import io.github.mityavasilyev.interviewunlimintparser.model.Currency;
import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.utils.OrderUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class JSONToOrderParser implements FileToOrdersParser {

    private final Path path;
    private final IdGenerator idGenerator;
    private List<Map<String,String>> orderDTOS;
    private final Integer initialEntriesAmount;

    public JSONToOrderParser(Path path, IdGenerator idGenerator) throws AccessDeniedException, FileNotFoundException {
        this.path = path;
        this.idGenerator = idGenerator;

        if (!Files.exists(path))
            throw new FileNotFoundException(String.format("No such file: %s", path.toAbsolutePath()));

        if (!Files.isReadable(path))
            throw new AccessDeniedException(String.format("Can't access file: %s", path.toAbsolutePath()));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String>[] rawObjects = objectMapper.readValue(path.toFile(), Map[].class);
            orderDTOS = new ArrayList<>(    // Kinda ugly workaround but most people use it on java8
                    Arrays.asList(rawObjects));
        } catch (IOException exception) {
            log.error("Failed to read data from provided file: {}", exception.getMessage());
            orderDTOS = new ArrayList<>();
        }
        initialEntriesAmount = orderDTOS.size();    // Needed for line property calculation. Json is tricky to read by lines
    }

    @Override
    public Order readNextOrder() {
        if (orderDTOS.isEmpty()) return null;
        Map<String, String> orderDetails = orderDTOS.remove(0);

        List<String> errors = new ArrayList<>();

        // Processing order ID
        long orderId;
        String orderIdField = String.valueOf(orderDetails.get("orderId"));
        try {
            orderId = Optional
                    .of(Long.parseLong(orderIdField))
                    .orElseThrow(() -> new IllegalArgumentException("No orderId provided"));
        } catch (Exception e) {
            log.warn("Failed to convert or find orderId: {}", orderIdField);
            errors.add(String.format("Unable to convert ID: %s", orderIdField));
            orderId = -1L;
        }

        // Processing amount
        double amount;
        String amountField = String.valueOf(orderDetails.get("amount"));
        try {
            amount = OptionalDouble
                    .of(Double.parseDouble(amountField))
                    .orElseThrow(() -> new IllegalArgumentException("No amount provided"));
        } catch (Exception e) {
            log.warn("Failed to convert currency: {}", amountField);
            errors.add(String.format("Unable to convert amount: %s", amountField));
            amount = 0D;
        }

        // Processing currency
        Currency currency;
        String currencyField = orderDetails.get("currency");
        try {
            currency = Optional
                    .of(Currency.valueOf(currencyField.toUpperCase(Locale.ROOT)))
                    .orElseThrow(() -> new IllegalArgumentException("No currency provided"));

        } catch (Exception e) {
            log.warn("Failed to convert currency: {}", currencyField);
            errors.add(String.format("Unable to convert currency: %s", currencyField));
            currency = Currency.UNKNOWN;
        }

        String comment = Optional.of(orderDetails
                .get("comment"))
                .orElseGet(() -> {
                    log.warn("Missing comment field");
                    errors.add("Missing comment field");
                    return "";
                });


        return Order.builder()
                .id(idGenerator.getNewId())
                .orderId(orderId)
                .amount(amount)
                .currency(currency)
                .comment(comment)
                .fileName(this.path.getFileName().toString())
                .line((long) (initialEntriesAmount - orderDTOS.size()))
                .parsingResult(OrderUtils.buildParseResult(errors))
                .build();

    }

}
