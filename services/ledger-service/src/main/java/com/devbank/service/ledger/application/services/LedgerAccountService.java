package com.devbank.service.ledger.application.services;

import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.application.event.producer.LedgerAccountCreatedProcessor;
import com.devbank.service.ledger.domain.entity.LedgerAccount;
import com.devbank.service.ledger.domain.enumeration.LedgerAccountStatus;
import com.devbank.service.ledger.infrastructure.repository.LedgerAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.ledger.process.LedgerAccountCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import java.util.List;
import java.util.UUID;

@Service
public class LedgerAccountService {
    private final LedgerAccountRepository repository;
    private final LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor;
    //todo object mapper will be replaced by another mapper
    private final ObjectMapper objectMapper;

    public LedgerAccountService(LedgerAccountRepository repository, LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor, ObjectMapper objectMapper) {
        this.repository = repository;
        this.ledgerAccountCreatedProcessor = ledgerAccountCreatedProcessor;
        this.objectMapper = objectMapper;
    }

    public List<LedgerAccountDto> getByReferenceId(String referenceId) {
        return repository
                .findByExternalRefId(referenceId)
                .stream()
                .map(a -> objectMapper.convertValue(a, LedgerAccountDto.class))
                .toList();
    }

    public List<LedgerAccountDto> getByReferenceIdAndDate(String referenceId, OffsetDateTime date) {
        return repository
                .findByExternalRefIdAndCreatedAtAfter(referenceId, date)
                .stream()
                .map(a -> objectMapper.convertValue(a, LedgerAccountDto.class))
                .toList();
    }

    @Transactional
    public LedgerAccountDto create(LedgerAccountDto model) {
        model.setId(null);
        LedgerAccount ledgerAccount = objectMapper.convertValue(model, LedgerAccount.class);
        LedgerAccount createdLedgerAccount = repository.save(ledgerAccount);

        ledgerAccountCreatedProcessor.process(new LedgerAccountCreatedEvent(createdLedgerAccount.getId()));
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
