package com.devbank.service.account.application.event.consumer;

import com.devbank.service.account.application.service.AccountService;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LedgerAccountCreatedConsumer implements Consumer<LedgerAccountCreatedEvent> {
    private final AccountService service;

    public LedgerAccountCreatedConsumer(AccountService service) {
        this.service = service;
    }

    @Override
    @KafkaListener(topics = "ledger-account.created", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<LedgerAccountCreatedEvent> payload) {
        log.info("Ledger Account Created Consumer was triggered");
        var event = payload.event();
        service.addLedgerIdIntoAccount(event.getAccountId(), event.getLedgerAccountId());
    }
}
