package com.devbank.service.ledger.application.services;

import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.domain.entity.LedgerAccount;
import com.devbank.service.ledger.infrastructure.repository.LedgerAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import java.util.List;
import java.util.UUID;

@Service
public class LedgerAccountService {
    private final LedgerAccountRepository repository;
    //todo object mapper will be replaced by another mapper
    private final ObjectMapper objectMapper;

    public LedgerAccountService(LedgerAccountRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public List<LedgerAccountDto> getByReferenceId(UUID referenceId) {
        return repository
                .findByExternalRefId(referenceId.toString())
                .stream()
                .map(a -> objectMapper.convertValue(a, LedgerAccountDto.class))
                .toList();
    }

    public List<LedgerAccountDto> getByReferenceIdAndDate(UUID referenceId, OffsetDateTime date) {
        return repository
                .findByExternalRefIdAndCreatedAtAfter(referenceId.toString(), date)
                .stream()
                .map(a -> objectMapper.convertValue(a, LedgerAccountDto.class))
                .toList();
    }

//    @Transactional
//    public List<LedgerAccountDto> create(LedgerAccountDto model) {
//        model.setId(null);
//        LedgerAccount ledgerAccount = objectMapper.convertValue(model, LedgerAccount.class);
//
//        LedgerAccount createdLedgerAccount = repository.save(ledgerAccount);
//
//
//    }
}
