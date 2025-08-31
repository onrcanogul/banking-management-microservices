package com.devbank.service.account.application.event.transactional.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.processor.Processor;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AccountCreatedProducer implements Processor<AccountCreatedEvent> {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public AccountCreatedProducer(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(AccountCreatedEvent event) {
        try {
            Outbox outbox = Outbox.builder()
                    .aggregateId(event.getAccountId().toString())
                    .createdAt(Instant.now())
                    .published(false)
                    .type(AccountCreatedEvent.class.getTypeName())
                    .payload(objectMapper.writeValueAsString(event))
                    .destination("account.created")
                    .build();
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
