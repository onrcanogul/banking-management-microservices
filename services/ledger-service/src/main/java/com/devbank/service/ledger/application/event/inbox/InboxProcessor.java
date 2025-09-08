package com.devbank.service.ledger.application.event.inbox;

import com.devbank.service.ledger.application.dto.CreateLedgerEntryDto;
import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.application.service.LedgerAccountService;
import com.devbank.service.ledger.application.service.LedgerEntryService;
import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.kafka.publisher.EventPublisher;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.messaging.event.transfer.TransferInitiatedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;
    private final LedgerAccountService ledgerAccountService;
    private final LedgerEntryService ledgerEntryService;

    public InboxProcessor(InboxRepository inboxRepository, ObjectMapper objectMapper, EventPublisher eventPublisher, LedgerAccountService ledgerAccountService, LedgerEntryService ledgerEntryService) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
        this.ledgerAccountService = ledgerAccountService;
        this.ledgerEntryService = ledgerEntryService;
    }

    public void process() throws BadRequestException, JsonProcessingException {
        List<Inbox> inboxes = inboxRepository.findByProcessedFalse();
        for (Inbox inbox: inboxes) {
            String type = inbox.getType();
            if(Objects.equals(type, TransferInitiatedEvent.class.getName())) {
                TransferInitiatedEvent event = objectMapper.convertValue(inbox.getPayload(), TransferInitiatedEvent.class);
                ledgerEntryService.create(CreateLedgerEntryDto.builder()
                        .transferId(event.getTransferId())
                        .description("TODO ADD DESCRIPTION")
                        .amount(event.getAmount())
                        .currency(event.getCurrency())
                        .externalRef(event.getExternalRef())
                        .fromAccountId(event.getFromAccountId())
                        .toAccountId(event.getToAccountId())
                        .build());
            }
            else if (Objects.equals(type, AccountCreatedEvent.class.getName())) {
                AccountCreatedEvent event = objectMapper.convertValue(inbox.getPayload(), AccountCreatedEvent.class);
                LedgerAccountDto ledgerAccountDto = LedgerAccountDto
                        .builder()
                        .currency(event.getCurrency())
                        .externalRefType("ACCOUNT")
                        .externalRefId(event.getAccountId().toString())
                        .status(LedgerAccountStatus.ACTIVE)
                        .build();
                ledgerAccountService.create(ledgerAccountDto);
            }
        }
    }
}
