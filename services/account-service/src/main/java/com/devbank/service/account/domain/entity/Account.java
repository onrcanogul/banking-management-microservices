package com.devbank.service.account.domain.entity;


import com.devbank.service.account.domain.enumeration.AccountStatus;
import com.template.core.audit.IInsertAuditing;
import com.template.core.audit.ISoftDelete;
import com.template.core.audit.IUpdateAuditing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "ACCOUNT",
        uniqueConstraints = @UniqueConstraint(name = "UK_ACCOUNT_IBAN", columnNames = "IBAN"),
        indexes = {
                @Index(name = "IX_ACCOUNT_CUSTOMER", columnList = "CUSTOMER_ID"),
                @Index(name = "IX_ACCOUNT_LEDGER_ACC", columnList = "LEDGER_ACCOUNT_ID")
        })
public class Account implements IInsertAuditing, IUpdateAuditing {
    @Id
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "RAW(16)")
    private UUID id;
    @Column(name = "CUSTOMER_ID", columnDefinition = "RAW(16)", nullable = false)
    private UUID customerId;
    @Column(name = "LEDGER_ACCOUNT_ID", columnDefinition = "RAW(16)", nullable = false)
    private UUID ledgerAccountId;
    @Column(name = "IBAN", columnDefinition = "VARCHAR2(34)", nullable = false)
    private String iban;
    @Column(name = "CURRENCY", columnDefinition = "CHAR(3)", nullable = false)
    private String currency;
    @Version @Column(name = "ROW_VERSION", columnDefinition = "NUMBER(19,0)")
    private int version;
    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;
}
