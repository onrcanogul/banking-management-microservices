package com.devbank.service.transaction.application.service;

import com.devbank.service.transaction.application.dto.TransactionDto;
import com.devbank.service.transaction.application.dto.TransferTransactionDto;
import com.devbank.service.transaction.domain.entity.Transaction;
import com.devbank.service.transaction.domain.entity.TransferTransaction;
import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import com.devbank.service.transaction.infrastructure.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionService(TransactionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getByAccount(UUID accountId) {
        return repository
                .findByAccount(accountId)
                .stream()
                .map(t -> objectMapper.convertValue(t, TransactionDto.class))
                .toList();
    }

    @Transactional
    public TransferTransactionDto updateStatus(UUID id, TransactionStatus status) {
        Transaction transfer = repository.findById(id).orElseThrow();
        transfer.setStatus(status);
        Transaction updatedTransfer = repository.save(transfer);
        return objectMapper.convertValue(updatedTransfer, TransferTransactionDto.class);
    }
}
