package com.devbank.service.ledger.application.event.producer;

import com.devbank.service.ledger.domain.entity.LedgerAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.processor.Processor;
import com.template.messaging.event.ledger.rollback.LedgerAccountNotFoundEvent;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LedgerAccountNotFoundEventProcessor implements Processor<LedgerAccountNotFoundEvent>{
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public LedgerAccountNotFoundEventProcessor(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(LedgerAccountNotFoundEvent event) {
        try {
            outboxRepository.save(Outbox.builder()
                    .type(LedgerAccountNotFoundEvent.class.getTypeName())
                    .aggregateId(event.getTransactionId().toString())
                    .destination("ledger-account.not-found")
                    .payload(objectMapper.writeValueAsString(event))
                    .published(false)
                    .aggregateType(LedgerAccount.class.getTypeName())
                    .createdAt(Instant.now())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
