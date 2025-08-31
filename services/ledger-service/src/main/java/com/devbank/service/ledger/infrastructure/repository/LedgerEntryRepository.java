package com.devbank.service.ledger.infrastructure.repository;

import com.devbank.service.ledger.domain.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    @Query("select a from LedgerEntry a where a.debitAccount.id = :externalId or a.creditAccount.id = :externalId")
    List<LedgerEntry> findByExternal(String externalId);

    @Query("select a from LedgerEntry a where a.debitAccount.id = :externalId")
    List<LedgerEntry> findByExpenses(String externalId);

    @Query("select a from LedgerEntry a where a.creditAccount.id = :externalId")
    List<LedgerEntry> findByCredits(String externalId);
}
