package com.devbank.service.transfer.domain.entity;


import com.devbank.service.transfer.domain.enumeration.TransferStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "transfer",
        indexes = {
                @Index(name = "ix_transfer_status", columnList = "status"),
                @Index(name = "ix_transfer_from",   columnList = "from_account_id"),
                @Index(name = "ix_transfer_to",     columnList = "to_account_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_transfer_external_ref", columnNames = {"external_ref"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Transfer {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "from_account_id", nullable = false, columnDefinition = "UUID")
    private UUID fromAccountId;

    @Column(name = "to_account_id", nullable = false, columnDefinition = "UUID")
    private UUID toAccountId;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 19, fraction = 4)
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "external_ref", length = 100)
    private String externalRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransferStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "posted_at")
    private OffsetDateTime postedAt;

    @Column(name = "failed_at")
    private OffsetDateTime failedAt;

    @Column(name = "canceled_at")
    private OffsetDateTime canceledAt;

    // Optimistic locking
    @Version
    @Column(name = "version")
    private Long version;

    public static Transfer create(UUID from, UUID to, String ccy, BigDecimal amt,
                                  String externalRef) {
        if (from.equals(to)) throw new IllegalArgumentException("from==to not allowed");
        Transfer t = new Transfer();
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

