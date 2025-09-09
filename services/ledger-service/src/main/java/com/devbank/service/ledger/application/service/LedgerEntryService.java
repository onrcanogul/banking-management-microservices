package com.devbank.service.ledger.application.service;

import com.devbank.service.ledger.application.dto.CreateLedgerEntryDto;
import com.devbank.service.ledger.application.dto.LedgerEntryDto;
import com.devbank.service.ledger.application.event.producer.LedgerAccountCreatedProcessor;
import com.devbank.service.ledger.application.event.transactional.transfer.LedgerAccountNotFoundEventProcessor;
import com.devbank.service.ledger.domain.entity.LedgerEntry;
import com.devbank.service.ledger.infrastructure.repository.LedgerAccountRepository;
import com.devbank.service.ledger.infrastructure.repository.LedgerEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.messaging.event.ledger.rollback.LedgerAccountNotFoundEvent;
import org.apache.coyote.BadRequestException;
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
    private final LedgerAccountNotFoundEventProcessor ledgerAccountNotFoundEventProcessor;
    private final LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor;

    public LedgerEntryService(LedgerEntryRepository repository, LedgerAccountRepository ledgerAccountRepository, ObjectMapper objectMapper, LedgerAccountNotFoundEventProcessor ledgerAccountNotFoundEventProcessor, LedgerAccountCreatedProcessor ledgerAccountCreatedProcessor) {
        this.repository = repository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.objectMapper = objectMapper;
        this.ledgerAccountNotFoundEventProcessor = ledgerAccountNotFoundEventProcessor;
        this.ledgerAccountCreatedProcessor = ledgerAccountCreatedProcessor;
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
    public LedgerEntryDto create(CreateLedgerEntryDto model) throws BadRequestException {
        var debitAccount = ledgerAccountRepository.findByExternalRefId(model.getFromAccountId().toString());
        var creditAccount = ledgerAccountRepository.findByExternalRefId(model.getToAccountId().toString());
        if (debitAccount == null || creditAccount == null) {
            ledgerAccountNotFoundEventProcessor.process(new LedgerAccountNotFoundEvent(model.getTransferId()));
            return null;
        }
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
        ledgerEntry.setType(model.getType());

        return ledgerEntry;
    }
}
