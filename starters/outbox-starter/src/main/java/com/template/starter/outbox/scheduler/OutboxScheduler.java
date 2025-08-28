package com.template.starter.outbox.scheduler;

import com.template.starter.outbox.processor.OutboxProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class OutboxScheduler {

    private final OutboxProcessor processor;
    private final boolean enabled;
    private final int maxBatchesPerTick;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public OutboxScheduler(
            OutboxProcessor processor,
            @Value("${outbox.scheduler.enabled:true}") boolean enabled,
            @Value("${outbox.scheduler.max-batches-per-tick:10}") int maxBatchesPerTick) {
        this.processor = processor;
        this.enabled = enabled;
        this.maxBatchesPerTick = maxBatchesPerTick;
    }

    @Scheduled(
            initialDelayString = "${outbox.scheduler.initial-delay:5s}",
            fixedDelayString   = "${outbox.scheduler.fixed-delay:2s}"
    )
    public void tick() {
        if (!enabled) return;
        if (!running.compareAndSet(false, true)) return; // overlap engelle

        try {
            int batches = 0;
            while (batches < maxBatchesPerTick) {
                processor.processAsync();
            }
        } catch (Exception e) {
            // top-level guard log
            System.err.println("OutboxScheduler failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            running.set(false);
        }
    }
}

//outbox:
//scheduler:
//enabled: true
//initial-delay: 5s
//fixed-delay: 2s
//batch-size: 100
//max-batches-per-tick: 10
//ack-timeout: 5s

