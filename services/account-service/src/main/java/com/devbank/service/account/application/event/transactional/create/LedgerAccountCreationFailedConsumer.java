package com.devbank.service.account.application.event.transactional.create;

import com.devbank.service.account.application.service.AccountService;
import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.ledger.rollback.LedgerAccountCreationFailedEvent;
import org.springframework.stereotype.Component;

@Component
public class LedgerAccountCreationFailedConsumer implements Consumer<LedgerAccountCreationFailedEvent> {
    private final AccountService service;

    public LedgerAccountCreationFailedConsumer(AccountService service) {
        this.service = service;
    }

    @Override
    public void consume(EventWrapper<LedgerAccountCreationFailedEvent> payload) {
        LedgerAccountCreationFailedEvent event = payload.event();
        service.delete(event.getAccountId());
    }
}
