package com.devbank.service.ledger.application.event.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.BadRequestException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InboxScheduler {
    private final InboxProcessor inboxProcessor;

    public InboxScheduler(InboxProcessor inboxProcessor) {
        this.inboxProcessor = inboxProcessor;
    }

    @Scheduled(fixedRate = 5000)
    public void process() throws BadRequestException, JsonProcessingException {
        inboxProcessor.process();
    }
}
