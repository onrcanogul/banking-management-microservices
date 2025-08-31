package com.devbank.service.transfer.application.dto;

import com.devbank.service.transfer.domain.enumeration.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TransferDto {
    private UUID id;
    private UUID fromAccountId;
    private UUID toAccountId;
    private String currency;
    private BigDecimal amount;
    private String externalRef;
    private TransferStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime postedAt;
    private OffsetDateTime failedAt;
    private OffsetDateTime canceledAt;
    private Long version;

    public static TransferDto create(UUID from, UUID to, String ccy, BigDecimal amt,
                                  String externalRef) {
        if (from.equals(to)) throw new IllegalArgumentException("from==to not allowed");
        TransferDto t = new TransferDto();
        t.id = UUID.randomUUID();
        t.fromAccountId = from;
        t.toAccountId = to;
        t.currency = ccy.toUpperCase();
        t.amount = amt;
        t.externalRef = externalRef;
        t.status = TransferStatus.PENDING;
        t.createdAt = OffsetDateTime.now();
        return t;
    }

    public void markPosted() {
        this.status = TransferStatus.POSTED;
        this.postedAt = OffsetDateTime.now();
        this.updatedAt = this.postedAt;
    }

    public void markFailed(String reason) {
        this.status = TransferStatus.FAILED;
        this.failedAt = OffsetDateTime.now();
        this.updatedAt = this.failedAt;
    }

    public void markCanceled(String reason) {
        this.status = TransferStatus.CANCELED;
        this.canceledAt = OffsetDateTime.now();
        this.updatedAt = this.canceledAt;
    }

    @PreUpdate
    void touch() { this.updatedAt = OffsetDateTime.now(); }
}
