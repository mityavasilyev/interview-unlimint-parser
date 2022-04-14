package io.github.mityavasilyev.interviewunlimintparser.extra;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class AtomicIdGenerator implements IdGenerator {

    private final static AtomicLong atomicLong = new AtomicLong(0L);

    public Long getNewId() {
        return atomicLong.getAndIncrement();
    }
}
