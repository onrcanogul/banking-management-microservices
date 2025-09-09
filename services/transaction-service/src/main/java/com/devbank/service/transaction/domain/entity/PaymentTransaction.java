package com.devbank.service.transaction.domain.entity;

import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("PAYMENT")
@Table(name = "payment_transaction")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PaymentTransaction extends Transaction {

    @Column(name = "payer_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID payerId;

    @Column(name = "merchant_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID merchantId;

    @Column(name = "bill_ref", nullable = false, length = 500)
    private String billRef;

    public static PaymentTransaction createPayment(UUID payerId, UUID merchantId, String billRef, String ccy, BigDecimal amt, String externalRef) {
        PaymentTransaction t = new PaymentTransaction();
        t.setId(UUID.randomUUID());
        t.payerId = payerId;
        t.merchantId = merchantId;
        t.billRef = billRef;
        t.setCurrency(ccy.toUpperCase());
        t.setAmount(amt);
        t.setExternalRef(externalRef);
        t.setStatus(TransactionStatus.PENDING);
        t.setCreatedAt(OffsetDateTime.now());
        return t;
    }
}

