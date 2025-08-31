package com.devbank.service.transfer.application.service;

import com.devbank.service.transfer.application.dto.CreateTransferDto;
import com.devbank.service.transfer.application.dto.TransferDto;
import com.devbank.service.transfer.application.event.transaction.initiate.TransferInitiatedProcessor;
import com.devbank.service.transfer.domain.entity.Transfer;
import com.devbank.service.transfer.domain.enumeration.TransferStatus;
import com.devbank.service.transfer.infrastructure.repository.TransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.transfer.TransferInitiatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository repository;
    // OM will replace with lighter mapper, because of reflection
    private final ObjectMapper objectMapper;
    private final TransferInitiatedProcessor transferInitiatedProcessor;

    public TransferService(TransferRepository repository, ObjectMapper objectMapper, TransferInitiatedProcessor transferInitiatedProcessor) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.transferInitiatedProcessor = transferInitiatedProcessor;
    }

    public TransferDto getById(UUID id) {
        Transfer transfer = repository.findById(id).orElseThrow();
        return objectMapper.convertValue(transfer, TransferDto.class);
    }

    public List<TransferDto> getByAccount(UUID accountId) {
        List<Transfer> transfers = repository.getByAccount(accountId);
        return transfers.stream().map(t -> objectMapper.convertValue(t, TransferDto.class)).toList();
    }

    @Transactional
    public TransferDto create(CreateTransferDto model) {
        Transfer transfer = Transfer.create(model.from(), model.to(), model.currency(), model.amount(), model.externalRef());
        Transfer createdTransfer = repository.save(transfer);
        transferInitiatedProcessor.process(new TransferInitiatedEvent(
                createdTransfer.getId(), createdTransfer.getFromAccountId(), createdTransfer.getToAccountId(),
                createdTransfer.getCurrency(), createdTransfer.getAmount(), createdTransfer.getExternalRef(),
                model.description()
        ));
        return objectMapper.convertValue(createdTransfer, TransferDto.class);
    }

    @Transactional
    public TransferDto updateStatus(UUID id, TransferStatus status) {
        Transfer transfer = repository.findById(id).orElseThrow();
        transfer.setStatus(status);
        Transfer updatedTransfer = repository.save(transfer);
        return objectMapper.convertValue(updatedTransfer, TransferDto.class);
    }
}
