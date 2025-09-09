package com.devbank.service.ledger.application.event.transactional.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.transaction.PaymentInitiatedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentInitiatedEventConsumer implements Consumer<PaymentInitiatedEvent> {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    public PaymentInitiatedEventConsumer(InboxRepository inboxRepository, ObjectMapper objectMapper) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(topics = "payment.initiated", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<PaymentInitiatedEvent> payload) {
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
