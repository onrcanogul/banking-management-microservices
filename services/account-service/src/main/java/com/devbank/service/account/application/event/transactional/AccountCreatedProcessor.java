package com.devbank.service.account.application.event.transactional;

import com.devbank.service.account.application.service.AccountService;
import com.template.messaging.event.account.process.AccountCreatedEvent;
import com.template.messaging.event.ledger.rollback.LedgerAccountCreationFailedEvent;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.transaction.SagaStep;
import org.springframework.transaction.annotation.Transactional;

public class AccountCreatedProcessor implements SagaStep<AccountCreatedEvent, LedgerAccountCreationFailedEvent> {
    private final AccountService accountService;

    public AccountCreatedProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void process(AccountCreatedEvent event) {
        /* write to outbox */
    }

    @Override
    @Transactional
    public void consume(EventWrapper<LedgerAccountCreationFailedEvent> payload) {
        LedgerAccountCreationFailedEvent event = payload.event();
        accountService.delete(event.getAccountId());
    }
}
