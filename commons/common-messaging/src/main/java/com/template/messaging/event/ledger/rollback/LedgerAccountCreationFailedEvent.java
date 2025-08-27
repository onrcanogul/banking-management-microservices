package com.template.messaging.event.ledger.rollback;

import com.template.messaging.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class LedgerAccountCreationFailedEvent implements Event {
    private UUID accountId;
}
