package com.devbank.service.ledger.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "LEDGER_ENTRY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UX_LE_UNIQ_REF", columnNames = {"REF_TYPE", "REF_ID"})
        },
        indexes = {
                @Index(name = "IX_LE_DEBIT_DT", columnList = "DEBIT_ACCOUNT, CREATED_AT"),
                @Index(name = "IX_LE_CREDIT_DT", columnList = "CREDIT_ACCOUNT, CREATED_AT"),
                @Index(name = "IX_LE_CREATED_DT", columnList = "CREATED_AT")
        }
)
public class LedgerEntry {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @ToString.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "ID", nullable = false, columnDefinition = "RAW(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEBIT_ACCOUNT", nullable = false,
            foreignKey = @ForeignKey(name = "FK_LE_DEBIT_ACC"))
    @ToString.Exclude
    private LedgerAccount debitAccount; //out

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CREDIT_ACCOUNT", nullable = false,
            foreignKey = @ForeignKey(name = "FK_LE_CREDIT_ACC"))
    @ToString.Exclude
    private LedgerAccount creditAccount; //in

    @Column(name = "AMOUNT", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;

    @Column(name = "REF_TYPE", nullable = false, length = 30)
    private String refType;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "REF_ID", nullable = false, length = 64)
    private String refId;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVERSAL_OF",
            foreignKey = @ForeignKey(name = "FK_LE_REVERSAL_OF"))
    @ToString.Exclude
    private LedgerEntry reversalOf;
}
