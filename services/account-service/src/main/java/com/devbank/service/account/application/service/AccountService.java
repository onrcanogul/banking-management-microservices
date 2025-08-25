package com.devbank.service.account.application.service;

import com.devbank.service.account.application.dto.AccountDto;
import com.devbank.service.account.domain.entity.Account;
import com.devbank.service.account.infrastructure.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final ObjectMapper objectMapper;

    public AccountService(AccountRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public AccountDto getById(UUID id) {
        Account account = repository.findById(id).orElseThrow();
        return objectMapper.convertValue(account, AccountDto.class);
    }

    public AccountDto getByCustomer(UUID customerId) {
        Account account = repository.getByCustomerId(customerId).orElseThrow();
        return objectMapper.convertValue(account, AccountDto.class);
    }

    public AccountDto create(AccountDto dto) {
        dto.setId(null);
        Account account = objectMapper.convertValue(dto, Account.class);
        return objectMapper.convertValue(repository.save(account), AccountDto.class);
    }

    public AccountDto update(AccountDto dto) {
        Account account = repository.findById(dto.getId()).orElseThrow();
        account.setCurrency(dto.getCurrency());
        account.setStatus(dto.getStatus());
        return objectMapper.convertValue(repository.save(account), AccountDto.class);
    }

    public void delete(UUID id) {
        Account account = repository.findById(id).orElseThrow();
        repository.delete(account);
    }
}
