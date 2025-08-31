package com.devbank.service.ledger.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateLedgerEntryDto {
    private UUID transferId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private String currency;
    private BigDecimal amount;
    private String description;
    private String externalRef;
}
