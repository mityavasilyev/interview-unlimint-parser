package io.github.mityavasilyev.interviewunlimintparser.parser;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;

public interface FileToOrdersParser {

    /**
     * Reads next order from the file. Assigns
     * provided id to an operation (parser itself does not manage ID assignment)
     *
     * @return next order. Returns null if reached the end of file
     */
    Order readNextOrder();
}
