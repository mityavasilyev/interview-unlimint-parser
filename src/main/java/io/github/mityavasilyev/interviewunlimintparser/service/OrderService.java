package io.github.mityavasilyev.interviewunlimintparser.service;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {

    /**
     * Gets a list or orders from a provided file.
     * Currently, supporting csv and json
     *
     * In order to be asynchronous returns a CompletableFuture
     *
     * @param path Path to a file to read data from
     * @return List of orders extracted from a file
     * @throws AccessDeniedException If file is protected
     * @throws FileNotFoundException If file was no found
     */
    CompletableFuture<List<Order>> getOrdersFromFile(Path path) throws AccessDeniedException, FileNotFoundException;
}
