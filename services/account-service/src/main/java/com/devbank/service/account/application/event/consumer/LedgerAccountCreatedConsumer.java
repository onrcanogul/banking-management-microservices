package com.devbank.service.account.application.event.consumer;

import com.devbank.service.account.application.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class LedgerAccountCreatedConsumer implements Consumer<LedgerAccountCreatedEvent> {
    private final AccountService service;
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    public LedgerAccountCreatedConsumer(AccountService service, InboxRepository inboxRepository, ObjectMapper objectMapper) {
        this.service = service;
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(topics = "ledger-account.created", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<LedgerAccountCreatedEvent> payload) {
        if (inboxRepository.findByIdempotentToken(payload.id()).isEmpty()) return;
        try {
            inboxRepository.save(
                    Inbox.builder()
                            .idempotentToken(payload.id())
                            .type(payload.type())
                            .payload(objectMapper.writeValueAsString(payload.event()))
                            .receivedAt(LocalDateTime.now())
                            .processed(false)
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
