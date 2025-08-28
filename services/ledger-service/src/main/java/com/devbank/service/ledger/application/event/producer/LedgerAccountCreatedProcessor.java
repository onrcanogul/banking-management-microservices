package com.devbank.service.ledger.application.event.producer;

import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class LedgerAccountCreatedProcessor {
    public void process(LedgerAccountCreatedEvent event) {
        /** write to outbox */
    }
}
