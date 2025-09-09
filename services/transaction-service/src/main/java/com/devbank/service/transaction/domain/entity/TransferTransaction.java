package com.devbank.service.transaction.domain.entity;

import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import com.devbank.service.transaction.domain.enumeration.TransactionType;
import com.devbank.service.transaction.domain.enumeration.TransferStatus;
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
@DiscriminatorValue("TRANSFER")
@Table(
        name = "transfer_transaction",
        indexes = {
                @Index(name = "ix_transfer_from", columnList = "from_account_id"),
                @Index(name = "ix_transfer_to", columnList = "to_account_id")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TransferTransaction extends Transaction {

    @Column(name = "from_account_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID fromAccountId;

    @Column(name = "to_account_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID toAccountId;

    public static TransferTransaction createTransfer(UUID from, UUID to, String ccy, BigDecimal amt, String externalRef) {
        if (from.equals(to)) throw new IllegalArgumentException("from==to not allowed");
        TransferTransaction t = new TransferTransaction();
        t.setId(UUID.randomUUID());
        t.fromAccountId = from;
        t.toAccountId = to;
        t.setCurrency(ccy.toUpperCase());
        t.setAmount(amt);
        t.setExternalRef(externalRef);
        t.setStatus(TransactionStatus.PENDING);
        t.setCreatedAt(OffsetDateTime.now());
        return t;
    }
}

