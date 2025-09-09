package com.devbank.service.transaction.infrastructure.repository;
import com.devbank.service.transaction.domain.entity.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, UUID> {
    @Query("""
       select t
       from TransferTransaction t
       where t.fromAccountId = :accountId
          or t.toAccountId = :accountId
       order by t.createdAt desc
       """)
    List<TransferTransaction> getByAccount(UUID accountId);
}
