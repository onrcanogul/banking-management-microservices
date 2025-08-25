package com.devbank.service.account.application.dto;

import com.devbank.service.account.domain.entity.Account;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AccountLimitDto {
    private UUID id;
    private Account account;
    private BigDecimal perTxLimit;
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
    private String currency;
    private OffsetDateTime effectiveFrom;
    private OffsetDateTime effectiveTo;
    private Long version;
    private boolean isDeleted;
}
