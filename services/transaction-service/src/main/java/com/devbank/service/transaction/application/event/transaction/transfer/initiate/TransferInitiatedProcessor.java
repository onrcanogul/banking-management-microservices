package com.devbank.service.transaction.application.event.transaction.transfer.initiate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.processor.Processor;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.messaging.event.transaction.TransferInitiatedEvent;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransferInitiatedProcessor implements Processor<TransferInitiatedEvent> {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public TransferInitiatedProcessor(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(TransferInitiatedEvent event) {
        try {
            Outbox outbox = Outbox.builder()
                    .aggregateId(event.getTransferId().toString())
                    .createdAt(Instant.now())
                    .published(false)
                    .type(AccountCreatedEvent.class.getTypeName())
                    .payload(objectMapper.writeValueAsString(event))
                    .destination("transfer.initiated")
                    .build();
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
