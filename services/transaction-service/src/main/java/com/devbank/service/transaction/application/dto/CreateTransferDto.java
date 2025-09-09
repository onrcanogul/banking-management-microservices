package com.devbank.service.transaction.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferDto (UUID from,
                                 UUID to,
                                 String currency,
                                 BigDecimal amount,
                                 String externalRef,
                                 String description
) {
}
