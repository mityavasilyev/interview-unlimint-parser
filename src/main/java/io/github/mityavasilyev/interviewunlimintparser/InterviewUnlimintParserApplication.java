package io.github.mityavasilyev.interviewunlimintparser;

import io.github.mityavasilyev.interviewunlimintparser.model.Order;
import io.github.mityavasilyev.interviewunlimintparser.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@SpringBootApplication
public class InterviewUnlimintParserApplication implements CommandLineRunner {

    private final OrderService orderService;

    public InterviewUnlimintParserApplication(OrderService orderService) {
        this.orderService = orderService;
    }

    public static void main(String[] args) {
        args = new String[]{"Testing", "Args"};  // TODO: 14.04.2022 Delete this
        SpringApplication.run(InterviewUnlimintParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Logging args
        StringBuilder passedArgs = new StringBuilder();
        if (args.length > 0)
            for (String arg: args)
                passedArgs.append(" | ").append(arg);
        log.info("Passed args: {}", passedArgs);

        List<Order> orderList = orderService.getOrdersFromFile(
                Paths.get("/Users/dmitry/Desktop/interview-unlimint.json"));

        for (Order order : orderList) {
            System.out.println(order);
        }

    }
}
