package com.template.messaging.event.ledger.process;

import com.template.messaging.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class LedgerAccountCreatedEvent implements Event {
    private UUID ledgerAccountId;
}
