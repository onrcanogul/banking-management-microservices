package com.template.messaging.event.ledger.rollback;

import com.template.messaging.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LedgerAccountNotFoundEvent implements Event {
    private UUID transferId;
}
