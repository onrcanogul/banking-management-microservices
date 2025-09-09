package com.devbank.service.transaction.application.service;

import com.devbank.service.transaction.application.dto.CreateTransferDto;
import com.devbank.service.transaction.application.dto.TransferTransactionDto;
import com.devbank.service.transaction.application.event.transaction.transfer.initiate.TransferInitiatedProcessor;
import com.devbank.service.transaction.domain.entity.TransferTransaction;
import com.devbank.service.transaction.domain.enumeration.TransactionStatus;
import com.devbank.service.transaction.infrastructure.repository.TransferTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.transaction.TransferInitiatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferService {
    private final TransferTransactionRepository repository;
    // OM will replace with lighter mapper, because of reflection
    private final ObjectMapper objectMapper;
    private final TransferInitiatedProcessor transferInitiatedProcessor;

    public TransferService(TransferTransactionRepository repository, ObjectMapper objectMapper, TransferInitiatedProcessor transferInitiatedProcessor) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.transferInitiatedProcessor = transferInitiatedProcessor;
    }

    public TransferTransactionDto getById(UUID id) {
        TransferTransaction transfer = repository.findById(id).orElseThrow();
        return objectMapper.convertValue(transfer, TransferTransactionDto.class);
    }


    @Transactional
    public TransferTransactionDto create(CreateTransferDto model) {
        TransferTransaction transfer = TransferTransaction.createTransfer(model.from(), model.to(), model.currency(), model.amount(), model.externalRef());
        TransferTransaction createdTransfer = repository.save(transfer);
        transferInitiatedProcessor.process(new TransferInitiatedEvent(
                createdTransfer.getId(), createdTransfer.getFromAccountId(), createdTransfer.getToAccountId(),
                createdTransfer.getCurrency(), createdTransfer.getAmount(), createdTransfer.getExternalRef(),
                model.description()
        ));
        return objectMapper.convertValue(createdTransfer, TransferTransactionDto.class);
    }

    @Transactional
    public TransferTransactionDto updateStatus(UUID id, TransactionStatus status) {
        TransferTransaction transfer = repository.findById(id).orElseThrow();
        transfer.setStatus(status);
        TransferTransaction updatedTransfer = repository.save(transfer);
        return objectMapper.convertValue(updatedTransfer, TransferTransactionDto.class);
    }
}
