package com.devbank.service.account.application.service;

import com.devbank.service.account.application.dto.AccountStatusHistoryDto;
import com.devbank.service.account.domain.entity.AccountStatusHistory;
import com.devbank.service.account.infrastructure.repository.AccountStatusHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountStatusHistoryService {

    private final AccountStatusHistoryRepository repository;
    private final ObjectMapper objectMapper;

    public AccountStatusHistoryService(AccountStatusHistoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public AccountStatusHistoryDto getByAccount(UUID accountId) {
        AccountStatusHistory history = repository.findByAccount_Id(accountId).orElseThrow();
        return objectMapper.convertValue(history, AccountStatusHistoryDto.class);
    }

    @Transactional
    public void createFromAccount(AccountStatusHistoryDto history) {
        history.setId(null);
        AccountStatusHistory accountStatusHistory = objectMapper.convertValue(history, AccountStatusHistory.class);
        repository.save(accountStatusHistory);
    }
}
