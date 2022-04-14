package io.github.mityavasilyev.interviewunlimintparser.service;

import io.github.mityavasilyev.interviewunlimintparser.exception.FileNotSupportedException;
import io.github.mityavasilyev.interviewunlimintparser.extra.IdGenerator;
import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.parser.CSVToOrderParser;
import io.github.mityavasilyev.interviewunlimintparser.parser.FileToOrdersParser;
import io.github.mityavasilyev.interviewunlimintparser.parser.JSONToOrderParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final IdGenerator idGenerator;

    @Override
    public List<Order> getOrdersFromFile(Path path) throws AccessDeniedException, FileNotFoundException {

        if (path.getFileName() == null) throw new FileNotFoundException(String.format("No such file: %s", path));

        String fileExtension = FilenameUtils.getExtension(String.valueOf(path.getFileName()));
        FileToOrdersParser ordersParser;
        switch (fileExtension.toLowerCase(Locale.ROOT)) {
            case "csv":
                ordersParser = new CSVToOrderParser(path, idGenerator);
                break;
            case "json":
                ordersParser = new JSONToOrderParser(path, idGenerator);
                break;
            default:
                throw new FileNotSupportedException(
                        String.format("No parser for [%s] extension implemented yet", fileExtension));
        }

        List<Order> orders = new ArrayList<>();
        Order nextOrder;
        try {
            while ((nextOrder = ordersParser.readNextOrder()) != null) {
                orders.add(nextOrder);
            }
        } catch (Exception exception) {
            log.error("Failed to parse entry in file: {}", path);
        }

        return orders;
    }
}
