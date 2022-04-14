package io.github.mityavasilyev.interviewunlimintparser.service;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {

    CompletableFuture<List<Order>> getOrdersFromFile(Path path) throws AccessDeniedException, FileNotFoundException;
}
