package com.devbank.service.transaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PaymentTransactionDto extends TransactionDto {
    private UUID id;
    private UUID merchantId;
    private String billRef;
}
