package com.devbank.service.account.application.dto;

import com.devbank.service.account.domain.enumeration.AccountStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class AccountStatusHistoryDto {
    private UUID id;
    private AccountDto account;
    private AccountStatus oldStatus;
    private AccountStatus newStatus;
    private OffsetDateTime changedAt;
    private String reason;
}
