package io.github.mityavasilyev.interviewunlimintparser.parser;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;

import java.nio.file.Path;
import java.util.List;

public interface OrderParser {

    List<Order> parseOrdersFromFile(Path filePath);
}
