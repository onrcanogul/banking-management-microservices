package com.devbank.service.ledger.application.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AccountCreatedConsumer implements Consumer<AccountCreatedEvent> {
    private final ObjectMapper objectMapper;
    private final InboxRepository inboxRepository;

    public AccountCreatedConsumer(ObjectMapper objectMapper, InboxRepository inboxRepository) {
        this.objectMapper = objectMapper;
        this.inboxRepository = inboxRepository;
    }

    @Override
    @KafkaListener(topics = "account.created", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<AccountCreatedEvent> payload) {
        if(inboxRepository.findByIdempotentToken(payload.id()).isEmpty()) return;
        try {
            inboxRepository.save(Inbox.builder()
                    .receivedAt(LocalDateTime.now())
                    .type(payload.type())
                    .processed(false)
                    .idempotentToken(payload.id())
                    .payload(objectMapper.writeValueAsString(payload.event()))
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
