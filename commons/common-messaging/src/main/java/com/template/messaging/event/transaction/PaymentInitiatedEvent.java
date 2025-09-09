package com.template.messaging.event.transaction;

import com.template.messaging.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PaymentInitiatedEvent implements Event {
    private UUID paymentId;
    private UUID payerId;
    private UUID merchantId;
    private String currency;
    private BigDecimal amount;
    private String externalRef;
    private String description;
}
