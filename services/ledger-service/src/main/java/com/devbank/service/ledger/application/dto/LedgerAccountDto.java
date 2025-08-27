package com.devbank.service.ledger.application.dto;

import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class LedgerAccountDto {
    private UUID id;
    private String externalRefType; // "ACCOUNT", "CARD", "LOAN" ...
    private String externalRefId;   // mostly accountId
    private String currency;
    private LedgerAccountStatus status = LedgerAccountStatus.ACTIVE;
    private OffsetDateTime createdAt;
    private boolean deleted = false;
}
