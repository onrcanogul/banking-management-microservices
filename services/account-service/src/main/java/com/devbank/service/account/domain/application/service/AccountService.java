package com.devbank.service.account.domain.application.service;

import com.devbank.service.account.infrastructure.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }


}
