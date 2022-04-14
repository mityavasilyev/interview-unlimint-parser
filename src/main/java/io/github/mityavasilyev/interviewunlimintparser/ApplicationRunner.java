package io.github.mityavasilyev.interviewunlimintparser;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationRunner implements CommandLineRunner {

    private final OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        // Logging args
        StringBuilder passedArgs = new StringBuilder();
        if (args.length > 0)
            for (String arg: args)
                passedArgs.append(" | ").append(arg);
        log.info("Passed args: {}", passedArgs);

        for (String arg : args) {
            orderService
                    .getOrdersFromFile(Paths.get(arg))
                    .thenAcceptAsync((List<Order> orders) -> {
                        for (Order order : orders) System.out.println(order);
                    });
        }
    }
}
