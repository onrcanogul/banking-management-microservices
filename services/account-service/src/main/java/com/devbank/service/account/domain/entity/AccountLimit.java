package com.devbank.service.account.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT_LIMIT",
        uniqueConstraints = @UniqueConstraint(name = "UK_ACC_LIMIT_ACCOUNT", columnNames = "ACCOUNT_ID"))
public class AccountLimit {

    @Id
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "RAW(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, columnDefinition = "RAW(16)",
            foreignKey = @ForeignKey(name = "FK_ACC_LIMIT_ACCOUNT"))
    private Account account;

    @Column(name = "PER_TX_LIMIT", precision = 19, scale = 4, nullable = false)
    private BigDecimal perTxLimit;

    @Column(name = "DAILY_LIMIT", precision = 19, scale = 4, nullable = false)
    private BigDecimal dailyLimit;

    @Column(name = "MONTHLY_LIMIT", precision = 19, scale = 4, nullable = false)
    private BigDecimal monthlyLimit;

    @Column(name = "CURRENCY", length = 3, nullable = false)
    private String currency;

    @Column(name = "EFFECTIVE_FROM")
    private OffsetDateTime effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private OffsetDateTime effectiveTo;

    @Version
    @Column(name = "ROW_VERSION")
    private Long version;
}
