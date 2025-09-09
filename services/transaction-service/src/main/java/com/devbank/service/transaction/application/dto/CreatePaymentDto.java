package com.devbank.service.transaction.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentDto(UUID payerId,
                               UUID merchantId,
                               String billRef,
                               String currency,
                               BigDecimal amount,
                               String externalRef,
                               String description
) {
}
