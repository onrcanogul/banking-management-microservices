package com.template.messaging.event.transfer;


import com.template.messaging.event.Event;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransferInitiatedEvent implements Event {
    private UUID transferId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private String currency;
    private BigDecimal amount;
    private String externalRef;
}
