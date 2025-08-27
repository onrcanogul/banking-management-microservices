package com.devbank.service.ledger.domain.entity;


import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.template.core.audit.IInsertAuditing;
import com.template.core.audit.ISoftDelete;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "LEDGER_ACCOUNT",
        indexes = {
                @Index(name = "IX_LA_ACTIVE", columnList = "IS_DELETED")
        }
)
public class LedgerAccount implements IInsertAuditing, ISoftDelete {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @ToString.Include
    @JdbcTypeCode(SqlTypes.BINARY) // RAW(16)
    @Column(name = "ID", nullable = false, columnDefinition = "RAW(16)")
    private UUID id;

    @ToString.Include
    @Column(name = "EXTERNAL_REF_TYPE", nullable = false, length = 30)
    private String externalRefType; // "ACCOUNT", "CARD", "LOAN" ...

    @ToString.Include
    @Column(name = "EXTERNAL_REF_ID", nullable = false, length = 64)
    private String externalRefId;   // mostly accountId

    @ToString.Include
    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private LedgerAccountStatus status = LedgerAccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;
}

