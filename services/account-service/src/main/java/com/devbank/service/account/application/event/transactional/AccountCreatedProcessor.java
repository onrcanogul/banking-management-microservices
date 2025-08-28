package com.devbank.service.account.application.event.transactional;

import com.devbank.service.account.application.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.messaging.event.ledger.rollback.LedgerAccountCreationFailedEvent;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.transaction.SagaStep;
import com.template.starter.outbox.entity.Outbox;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class AccountCreatedProcessor implements SagaStep<AccountCreatedEvent, LedgerAccountCreationFailedEvent> {
    private final AccountService accountService;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public AccountCreatedProcessor(AccountService accountService, ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.accountService = accountService;
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
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

    @Override
    @Transactional
    @KafkaListener(topics = "ledger.creation.failed")
    public void consume(EventWrapper<LedgerAccountCreationFailedEvent> payload) {
        LedgerAccountCreationFailedEvent event = payload.event();
        accountService.delete(event.getAccountId());
    }
}
