package com.devbank.service.transaction.application.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.ledger.rollback.LedgerAccountNotFoundEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LedgerAccountNotFoundEventConsumer implements Consumer<LedgerAccountNotFoundEvent> {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    public LedgerAccountNotFoundEventConsumer(InboxRepository inboxRepository, ObjectMapper objectMapper) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(topics = "ledger-account.not-found", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<LedgerAccountNotFoundEvent> payload) {
        if (inboxRepository.findByIdempotentToken(payload.id()).isPresent()) return;
        try {
            inboxRepository.save(Inbox.builder()
                            .idempotentToken(payload.id())
                            .type(payload.type())
                            .processed(false)
                            .payload(objectMapper.writeValueAsString(payload.event()))
                            .receivedAt(LocalDateTime.now())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
