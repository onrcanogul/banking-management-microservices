package com.devbank.service.ledger.application.event.producer;

import com.devbank.service.ledger.domain.entity.LedgerAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LedgerAccountCreatedProcessor {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public LedgerAccountCreatedProcessor(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public void process(LedgerAccountCreatedEvent event) throws JsonProcessingException {
        outboxRepository.save(Outbox.builder()
                .type(LedgerAccountCreatedEvent.class.getTypeName())
                .aggregateType(LedgerAccount.class.getTypeName())
                .destination("ledger-account.created")
                .payload(objectMapper.writeValueAsString(event))
                .createdAt(Instant.now())
                .published(false)
                .aggregateId(String.valueOf(event.getLedgerAccountId()))
                .build());
    }
}
