package com.sd.bank.repository;

import com.sd.bank.entity.BankTransaction;
import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByAccountId(Long accountId);

    List<BankTransaction> findByAccountIdAndTransactionType(
            Long accountId,
            TransactionType transactionType
    );

    List<BankTransaction> findByAccountIdAndTransactionStatus(
            Long accountId,
            TransactionStatus status
    );

    Optional<BankTransaction> findByTransactionReference(
            String transactionReference
    );

    boolean existsByTransactionReference(
            String transactionReference
    );
}
