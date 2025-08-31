package com.devbank.service.ledger.application.event.transactional.transfer;

import com.devbank.service.ledger.application.dto.CreateLedgerEntryDto;
import com.devbank.service.ledger.application.services.LedgerEntryService;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.transfer.TransferInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TransferInitiatedEventConsumer implements Consumer<TransferInitiatedEvent> {
    private final LedgerEntryService service;

    public TransferInitiatedEventConsumer(LedgerEntryService service) {
        this.service = service;
    }

    @Override
    @KafkaListener(topics = "transfer.initiated", containerFactory = "kafkaListenerContainerFactory")
    public void consume(EventWrapper<TransferInitiatedEvent> payload) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
