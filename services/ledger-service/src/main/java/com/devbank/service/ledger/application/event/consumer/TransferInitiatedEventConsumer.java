package com.devbank.service.ledger.application.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.transaction.TransferInitiatedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferInitiatedEventConsumer implements Consumer<TransferInitiatedEvent> {
    private final ObjectMapper objectMapper;
    private final InboxRepository inboxRepository;

    public TransferInitiatedEventConsumer(ObjectMapper objectMapper, InboxRepository inboxRepository) {
        this.objectMapper = objectMapper;
        this.inboxRepository = inboxRepository;
    }

    @Override
    @KafkaListener(topics = "transfer.initiated", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<TransferInitiatedEvent> payload) {
        if (inboxRepository.findByIdempotentToken(payload.id()).isPresent()) return;
        try {
            inboxRepository.save(Inbox.builder()
                    .idempotentToken(payload.id())
                    .type(payload.type())
                    .processed(false)
                    .receivedAt(LocalDateTime.now())
                    .payload(objectMapper.writeValueAsString(payload.event()))
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
