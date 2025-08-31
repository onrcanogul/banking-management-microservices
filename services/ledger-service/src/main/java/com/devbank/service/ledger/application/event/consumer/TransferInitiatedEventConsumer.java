package com.devbank.service.ledger.application.event.consumer;

import com.devbank.service.ledger.application.dto.CreateLedgerEntryDto;
import com.devbank.service.ledger.application.services.LedgerEntryService;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.transfer.TransferInitiatedEvent;

public class TransferInitiatedEventConsumer implements Consumer<TransferInitiatedEvent> {
    private final LedgerEntryService service;

    public TransferInitiatedEventConsumer(LedgerEntryService service) {
        this.service = service;
    }

    @Override
    public void consume(EventWrapper<TransferInitiatedEvent> payload) {
        var event = payload.event();
        service.create(CreateLedgerEntryDto.builder()
                        .transferId(event.getTransferId())
                        .description("TODO ADD DESCRIPTION")
                        .amount(event.getAmount())
                        .currency(event.getCurrency())
                        .externalRef(event.getExternalRef())
                        .fromAccountId(event.getFromAccountId())
                        .toAccountId(event.getToAccountId())
                .build());
    }
}
