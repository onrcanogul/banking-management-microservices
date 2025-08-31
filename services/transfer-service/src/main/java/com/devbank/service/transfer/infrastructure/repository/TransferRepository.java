package com.devbank.service.transfer.infrastructure.repository;

import com.devbank.service.transfer.domain.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    @Query("""
       select t
       from Transfer t
       where t.fromAccountId = :accountId
          or t.toAccountId = :accountId
       order by t.createdAt desc
       """)
    List<Transfer> getByAccount(UUID accountId);
}
