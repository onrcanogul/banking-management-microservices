package com.devbank.service.ledger.infrastructure.repository;

import com.devbank.service.ledger.application.dto.LedgerAccountDto;
import com.devbank.service.ledger.domain.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
    List<LedgerAccount> findByExternalRefId(String externalRefId);
    List<LedgerAccount> findByExternalRefIdAndCreatedAtAfter(String externalRefId, OffsetDateTime createdAt);
}
