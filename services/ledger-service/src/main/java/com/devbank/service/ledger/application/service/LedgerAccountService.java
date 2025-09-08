package com.devbank.service.ledger.application.service;

import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.application.event.producer.LedgerAccountCreatedProcessor;
import com.devbank.service.ledger.domain.entity.LedgerAccount;
import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.devbank.service.ledger.infrastructure.repository.LedgerAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import com.template.starter.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class LedgerAccountService {
    private final LedgerAccountRepository repository;
    private final LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor;
    private final OutboxRepository outboxRepository;
    //todo object mapper will be replaced by another mapper
    private final ObjectMapper objectMapper;

    public LedgerAccountService(LedgerAccountRepository repository, LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.ledgerAccountCreatedProcessor = ledgerAccountCreatedProcessor;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public LedgerAccountDto getByReferenceId(String referenceId) {
        return objectMapper.convertValue(repository.findByExternalRefId(referenceId), LedgerAccountDto.class);
    }

    public List<LedgerAccountDto> getByReferenceIdAndDate(String referenceId, OffsetDateTime date) {
        return repository
                .findByExternalRefIdAndCreatedAtAfter(referenceId, date)
                .stream()
                .map(a -> objectMapper.convertValue(a, LedgerAccountDto.class))
                .toList();
    }

    @Transactional
    public LedgerAccountDto create(LedgerAccountDto model) throws JsonProcessingException {
        model.setId(null);
        LedgerAccount ledgerAccount = objectMapper.convertValue(model, LedgerAccount.class);
        LedgerAccount createdLedgerAccount = repository.save(ledgerAccount);
        if(Objects.equals(model.getExternalRefType(), "ACCOUNT")) {
            ledgerAccountCreatedProcessor.process(new LedgerAccountCreatedEvent(createdLedgerAccount.getId(), UUID.fromString(model.getExternalRefId()))); // save outbox
        }
        return objectMapper.convertValue(model, LedgerAccountDto.class);
    }

    @Transactional
    public LedgerAccountDto updateStatus(UUID ledgerAccountId, LedgerAccountStatus status) {
        LedgerAccount ledgerAccount = repository.findById(ledgerAccountId).orElseThrow();
        ledgerAccount.setStatus(status);
        LedgerAccount savedLedgerAccount = repository.save(ledgerAccount);
        return objectMapper.convertValue(savedLedgerAccount, LedgerAccountDto.class);
    }
}
