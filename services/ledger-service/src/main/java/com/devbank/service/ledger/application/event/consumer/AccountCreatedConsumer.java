package com.devbank.service.ledger.application.event.consumer;

import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.application.services.LedgerAccountService;
import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountCreatedConsumer implements Consumer<AccountCreatedEvent> {
    private final LedgerAccountService ledgerAccountService;

    public AccountCreatedConsumer(LedgerAccountService ledgerAccountService) {
        this.ledgerAccountService = ledgerAccountService;
    }

    @Override
    @KafkaListener(topics = "account.created", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<AccountCreatedEvent> payload) {
        try {
            var event = payload.event();
            LedgerAccountDto ledgerAccountDto = LedgerAccountDto
                    .builder()
                    .currency(event.getCurrency())
                    .externalRefType("ACCOUNT")
                    .externalRefId(event.getAccountId().toString())
                    .status(LedgerAccountStatus.ACTIVE)
                    .build();
            ledgerAccountService.create(ledgerAccountDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
