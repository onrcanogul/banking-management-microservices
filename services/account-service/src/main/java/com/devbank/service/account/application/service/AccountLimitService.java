package com.devbank.service.account.application.service;

import com.devbank.service.account.application.dto.AccountLimitDto;
import com.devbank.service.account.domain.entity.AccountLimit;
import com.devbank.service.account.infrastructure.repository.AccountLimitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountLimitService {
    private final AccountLimitRepository repository;
    private final ObjectMapper objectMapper;

    public AccountLimitService(AccountLimitRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public AccountLimitDto getById(UUID id) {
        AccountLimit accountLimit = repository.findById(id).orElseThrow();
        return objectMapper.convertValue(accountLimit, AccountLimitDto.class);
    }

    public AccountLimitDto getByAccount(UUID accountId) {
        AccountLimit accountLimit = repository.findByAccount_Id(accountId).orElseThrow();
        return objectMapper.convertValue(accountLimit, AccountLimitDto.class);
    }

    public AccountLimitDto create(AccountLimitDto dto) {
        dto.setId(null);
        AccountLimit accountLimit = objectMapper.convertValue(dto, AccountLimit.class);
        return objectMapper.convertValue(repository.save(accountLimit), AccountLimitDto.class);
    }

    public AccountLimitDto update(AccountLimitDto dto) {
        AccountLimit accountLimit = repository.findById(dto.getId()).orElseThrow();
        accountLimit.setDailyLimit(dto.getDailyLimit());
        accountLimit.setMonthlyLimit(dto.getMonthlyLimit());
        accountLimit.setCurrency(dto.getCurrency());
        accountLimit.setPerTxLimit(dto.getPerTxLimit());
        accountLimit.setEffectiveTo(dto.getEffectiveTo());
        accountLimit.setEffectiveFrom(dto.getEffectiveFrom());
        return objectMapper.convertValue(repository.save(accountLimit), AccountLimitDto.class);
    }

    public void delete(UUID id) {
        AccountLimit accountLimit = repository.findById(id).orElseThrow();
        repository.delete(accountLimit);
    }
}
