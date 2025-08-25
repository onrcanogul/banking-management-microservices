package com.devbank.service.account.domain.application.dto;

import com.devbank.service.account.domain.enumeration.AccountStatus;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private UUID customerId;
    private UUID ledgerAccountId;
    private String iban;
    private String currency;
    private int version;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private AccountStatus status = AccountStatus.ACTIVE;
}
