package com.devbank.service.transaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TransferTransactionDto extends TransactionDto {
    private UUID id;
    private UUID fromAccountId;
    private UUID toAccountId;
}
