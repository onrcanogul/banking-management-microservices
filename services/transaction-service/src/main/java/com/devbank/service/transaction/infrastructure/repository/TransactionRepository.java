package com.devbank.service.transaction.infrastructure.repository;

import com.devbank.service.transaction.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""
       select t from Transaction t
       left join TransferTransaction tr on t.id = tr.id
       left join PaymentTransaction p on t.id = p.id
       where tr.fromAccountId = :accountId
          or tr.toAccountId = :accountId
          or p.payerId = :accountId
       """)
    List<Transaction> findByAccount(UUID accountId);
}
