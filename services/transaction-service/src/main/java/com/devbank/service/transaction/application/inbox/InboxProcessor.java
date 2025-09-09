package com.devbank.service.transaction.application.inbox;

import com.devbank.service.transaction.application.service.TransactionService;
import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.kafka.publisher.EventPublisher;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import com.template.messaging.event.ledger.rollback.LedgerAccountCreationFailedEvent;
import com.template.messaging.event.ledger.rollback.LedgerAccountNotFoundEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    public InboxProcessor(InboxRepository inboxRepository, ObjectMapper objectMapper, EventPublisher eventPublisher, TransactionService transactionService) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
    }

    public void process() throws JsonProcessingException {
        List<Inbox> inboxes = inboxRepository.findByProcessedFalse();
        for (Inbox inbox: inboxes) {
            String type = inbox.getType();
            if (Objects.equals(type, LedgerAccountNotFoundEvent.class.getName())) {
               LedgerAccountNotFoundEvent ledgerAccountNotFoundEvent = objectMapper.readValue(inbox.getPayload(), LedgerAccountNotFoundEvent.class);
               transactionService.updateStatus(ledgerAccountNotFoundEvent.getTransactionId(), TransactionStatus.FAILED);
            }

        }
    }
}
