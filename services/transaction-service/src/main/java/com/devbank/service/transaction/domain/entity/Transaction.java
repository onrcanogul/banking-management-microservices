package com.devbank.service.transaction.domain.entity;

import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
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
        name = "transaction",
        indexes = {
                @Index(name = "ix_transaction_status", columnList = "status"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_transaction_external_ref", columnNames = {"external_ref"})
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class Transaction {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "RAW(16)")
    private UUID id;

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
    private TransactionStatus status;

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

    @Version
    @Column(name = "version")
    private Long version;

    @PreUpdate
    void touch() { this.updatedAt = OffsetDateTime.now(); }
}


