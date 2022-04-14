package io.github.mityavasilyev.interviewunlimintparser;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationRunner implements CommandLineRunner {

    private final OrderService orderService;
    private final ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {

        // Will keep track of all futures
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String arg : args) {
            CompletableFuture<Void> parsingProcess = orderService
                    .getOrdersFromFile(Paths.get(arg))
                    .thenAcceptAsync(orders -> {
                        // Printing list once done parsing
                        for (Order order : orders) System.out.println(order);
                    });
            futures.add(parsingProcess);
        }

        // From here it's just few lines for proper program termination
        CompletableFuture<Void> godFatherOfFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]));

        // Checking every second whether parsing processes are finished
        while (!godFatherOfFutures.isDone()) {
            Thread.sleep(1000); // Yes, I know it's busy-waiting. But that's intentional
        }
        Thread.sleep(2000);
        ((ConfigurableApplicationContext) context).close();
    }
}
