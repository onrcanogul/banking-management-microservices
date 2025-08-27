package com.devbank.service.ledger.application.dto;

import com.devbank.service.ledger.domain.entity.LedgerAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@Builder
public class LedgerEntryDto {
    private UUID id;
    private LedgerAccount debitAccount; //out
    private LedgerAccount creditAccount; //in
    private BigDecimal amount;
    private String currency;
    private String refType;
    private String refId;
    private String description;
    private OffsetDateTime createdAt;
    private LedgerEntryDto reversalOf;
}
