package com.devbank.service.account.infrastructure.repository;

import com.devbank.service.account.domain.entity.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountLimitRepository extends JpaRepository<AccountLimit, UUID> {
}
