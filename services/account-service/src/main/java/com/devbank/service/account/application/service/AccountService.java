package com.devbank.service.account.application.service;

import com.devbank.service.account.application.dto.AccountDto;
import com.devbank.service.account.application.dto.AccountStatusHistoryDto;
import com.devbank.service.account.application.dto.UpdateStatusDto;
import com.devbank.service.account.domain.entity.Account;
import com.devbank.service.account.domain.enumeration.AccountStatus;
import com.devbank.service.account.infrastructure.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final ObjectMapper objectMapper;
    private final AccountStatusHistoryService accountStatusHistoryService;

    public AccountService(AccountRepository repository, ObjectMapper objectMapper, AccountStatusHistoryService accountStatusHistoryService) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.accountStatusHistoryService = accountStatusHistoryService;
    }

    public AccountDto getById(UUID id) {
        Account account = repository.findById(id).orElseThrow();
        return objectMapper.convertValue(account, AccountDto.class);
    }

    public AccountDto getByCustomer(UUID customerId) {
        Account account = repository.getByCustomerId(customerId).orElseThrow();
        return objectMapper.convertValue(account, AccountDto.class);
    }

    @Transactional
    public AccountDto create(AccountDto dto) {
        dto.setId(null);
        Account account = objectMapper.convertValue(dto, Account.class);
        Account createdAccount = repository.save(account);
        AccountDto accountDto = objectMapper.convertValue(createdAccount, AccountDto.class);

        accountStatusHistoryService.createFromAccount(AccountStatusHistoryDto.builder()
                .account(accountDto)
                .changedAt(OffsetDateTime.now())
                .newStatus(AccountStatus.ACTIVE)
                .oldStatus(AccountStatus.CLOSED)
                .reason("First Creation")
                .build());

        return accountDto;
    }

    public AccountDto update(AccountDto dto) {
        Account account = repository.findById(dto.getId()).orElseThrow();
        account.setCurrency(dto.getCurrency());
        account.setStatus(dto.getStatus());
        return objectMapper.convertValue(repository.save(account), AccountDto.class);
    }

    @Transactional
    public AccountDto updateStatus(UpdateStatusDto model) {
        Account account = repository.findById(model.getId()).orElseThrow();
        if (account.getStatus() != model.getStatus()) {
            account.setStatus(model.getStatus());
            Account createdAccount = repository.save(account);
            accountStatusHistoryService.createFromAccount(AccountStatusHistoryDto.builder()
                    .account(objectMapper.convertValue(account, AccountDto.class))
                    .changedAt(OffsetDateTime.now())
                    .newStatus(model.getStatus())
                    .oldStatus(account.getStatus())
                    .reason(model.getReason())
                    .build());
            return objectMapper.convertValue(createdAccount, AccountDto.class);
        }
        return objectMapper.convertValue(account, AccountDto.class);

    }

    public void delete(UUID id) {
        Account account = repository.findById(id).orElseThrow();
        repository.delete(account);
    }
}
