package com.devbank.service.transaction.application.dto;

import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TransactionDto {
    private String currency;
    private BigDecimal amount;
    private String externalRef;
    private TransactionStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime postedAt;
    private OffsetDateTime failedAt;
    private OffsetDateTime canceledAt;
    private Long version;
}
