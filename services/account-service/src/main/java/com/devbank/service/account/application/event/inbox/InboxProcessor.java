package com.devbank.service.account.application.event.inbox;

import com.devbank.service.account.application.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.kafka.publisher.EventPublisher;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import com.template.messaging.event.ledger.rollback.LedgerAccountCreationFailedEvent;
import com.template.starter.inbox.entity.Inbox;
import com.template.starter.inbox.repository.InboxRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;
    private final AccountService accountService;

    public InboxProcessor(InboxRepository inboxRepository, ObjectMapper objectMapper, EventPublisher eventPublisher, AccountService accountService) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
        this.accountService = accountService;
    }

    public void process() {
        List<Inbox> inboxes = inboxRepository.findByProcessedFalse();
        for (Inbox inbox: inboxes) {
            String type = inbox.getType();
            if(Objects.equals(type, LedgerAccountCreatedEvent.class.getName())) {
                LedgerAccountCreatedEvent event = objectMapper.convertValue(inbox.getPayload(), LedgerAccountCreatedEvent.class);
                accountService.addLedgerIdIntoAccount(event.getAccountId(), event.getLedgerAccountId());
            }
            else if (Objects.equals(type, LedgerAccountCreationFailedEvent.class.getName())) {
                LedgerAccountCreationFailedEvent event = objectMapper.convertValue(inbox.getPayload(), LedgerAccountCreationFailedEvent.class);
                accountService.delete(event.getAccountId());
            }
        }
    }
}
