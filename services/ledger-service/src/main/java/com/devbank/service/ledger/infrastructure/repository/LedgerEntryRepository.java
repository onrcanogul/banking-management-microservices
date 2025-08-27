package com.devbank.service.ledger.infrastructure.repository;

import com.devbank.service.ledger.domain.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
}
