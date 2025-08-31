package com.devbank.service.ledger.application.services;

import com.devbank.service.ledger.application.dto.CreateLedgerEntryDto;
import com.devbank.service.ledger.application.dto.LedgerEntryDto;
import com.devbank.service.ledger.domain.entity.LedgerEntry;
import com.devbank.service.ledger.infrastructure.repository.LedgerAccountRepository;
import com.devbank.service.ledger.infrastructure.repository.LedgerEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerEntryService {
    private final LedgerEntryRepository repository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final ObjectMapper objectMapper;

    public LedgerEntryService(LedgerEntryRepository repository, LedgerAccountRepository ledgerAccountRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.objectMapper = objectMapper;
    }

    public LedgerEntryDto getById(UUID id) {
        return objectMapper.convertValue(repository.findById(id).orElseThrow(), LedgerEntryDto.class);
    }

    public List<LedgerEntryDto> getByExternalRefId(String externalRefId) {
        return repository.findByExternal(externalRefId).stream()
                .map(l -> objectMapper.convertValue(l, LedgerEntryDto.class)).toList();
    }

    public List<LedgerEntryDto> getExpenses(String externalRefId) {
        return repository.findByExpenses(externalRefId).stream()
                .map(l -> objectMapper.convertValue(l, LedgerEntryDto.class)).toList();
    }

    public List<LedgerEntryDto> getCredits(String externalRefId) {
        return repository.findByCredits(externalRefId).stream()
                .map(l -> objectMapper.convertValue(l, LedgerEntryDto.class)).toList();
    }

    @Transactional
    public LedgerEntryDto create(CreateLedgerEntryDto model) {
        LedgerEntry transfer = repository.save(factory(model));
        return objectMapper.convertValue(transfer, LedgerEntryDto.class);
    }

    private LedgerEntry factory(CreateLedgerEntryDto model) {
        LedgerEntry ledgerEntry = new LedgerEntry();
        ledgerEntry.setAmount(model.getAmount());
        ledgerEntry.setCurrency(model.getCurrency());
        ledgerEntry.setDebitAccount(ledgerAccountRepository.getReferenceById(model.getFromAccountId()));
        ledgerEntry.setCreditAccount(ledgerAccountRepository.getReferenceById(model.getToAccountId()));
        ledgerEntry.setRefType(model.getExternalRef());
        ledgerEntry.setRefId(model.getExternalRef());
        ledgerEntry.setDescription(model.getDescription());
        ledgerEntry.setCreatedAt(OffsetDateTime.now());

        return ledgerEntry;
    }
}
