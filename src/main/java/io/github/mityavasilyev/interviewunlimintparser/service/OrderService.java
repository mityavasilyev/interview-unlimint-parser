package io.github.mityavasilyev.interviewunlimintparser.service;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.List;

public interface OrderService {

    List<Order> getOrdersFromFile(Path path) throws AccessDeniedException, FileNotFoundException;
}
