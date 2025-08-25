package com.devbank.service.account.domain.entity;

import com.devbank.service.account.domain.enumeration.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "ACCOUNT_STATUS_HISTORY",
        indexes = @Index(name = "IX_ACC_STATUS_ACCOUNT", columnList = "ACCOUNT_ID"))
public class AccountStatusHistory {

    @Id
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "RAW(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, columnDefinition = "RAW(16)",
            foreignKey = @ForeignKey(name = "FK_ACC_STATUS_ACCOUNT"))
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "OLD_STATUS", length = 20, nullable = false)
    private AccountStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "NEW_STATUS", length = 20, nullable = false)
    private AccountStatus newStatus;

    @Column(name = "CHANGED_AT", nullable = false)
    private OffsetDateTime changedAt;

    @Column(name = "REASON", length = 200)
    private String reason;
}

