package com.sd.bank.service;


import com.sd.bank.dto.MoneyRequest;
import com.sd.bank.dto.TransactionResponse;
import com.sd.bank.entity.Account;
import com.sd.bank.entity.BankTransaction;
import com.sd.bank.enums.AccountStatus;
import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import com.sd.bank.exception.ResourceNotFoundException;
import com.sd.bank.repository.AccountRepository;
import com.sd.bank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private final AccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse deposit(
            Long accountId,
            MoneyRequest request
    ) {

        validateAmount(request.getAmount());

        /*
         * PESSIMISTIC WRITE LOCK
         *
         * Account row remains locked until transaction completes.
         */
        Account account = accountRepository
                .findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountId
                        ));

        validateAccount(account);

        BigDecimal amount = request.getAmount();

        /*
         * Increase balance.
         */
        account.setBalance(
                account.getBalance().add(amount)
        );

        /*
         * Create transaction record.
         */
        BankTransaction transaction =
                BankTransaction.builder()
                        .transactionReference(generateReference())
                        .account(account)
                        .amount(amount)
                        .transactionType(TransactionType.CREDIT)
                        .transactionStatus(TransactionStatus.SUCCESS)
                        .description(
                                request.getDescription() != null
                                        ? request.getDescription()
                                        : "Account deposit"
                        )
                        .build();

        transactionRepository.save(transaction);

        /*
         * No explicit accountRepository.save() is required
         * because account is a managed JPA entity.
         *
         * Hibernate dirty checking will update it.
         */

        return mapToResponse(transaction);
    }


    @Transactional
    public TransactionResponse withdraw(
            Long accountId,
            MoneyRequest request
    ) {

        validateAmount(request.getAmount());

        Account account = accountRepository
                .findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountId
                        ));

        validateAccount(account);

        BigDecimal amount = request.getAmount();

        /*
         * Check balance while the account is locked.
         */
        if (account.getBalance().compareTo(amount) < 0) {

            throw new IllegalStateException(
                    "Insufficient balance. Available: "
                            + account.getBalance()
                            + ", Requested: "
                            + amount
            );
        }

        /*
         * Decrease balance.
         */
        account.setBalance(
                account.getBalance().subtract(amount)
        );

        /*
         * Create DEBIT transaction.
         */
        BankTransaction transaction =
                BankTransaction.builder()
                        .transactionReference(generateReference())
                        .account(account)
                        .amount(amount)
                        .transactionType(TransactionType.DEBIT)
                        .transactionStatus(TransactionStatus.SUCCESS)
                        .description(
                                request.getDescription() != null
                                        ? request.getDescription()
                                        : "Account withdrawal"
                        )
                        .build();

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }


    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }


    private void validateAccount(Account account) {

        if (account.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Account is not active: "
                            + account.getAccountNumber()
            );
        }
    }


    private String generateReference() {

        return "TXN-" + UUID.randomUUID();
    }


    private TransactionResponse mapToResponse(
            BankTransaction transaction
    ) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .accountId(
                        transaction.getAccount().getId()
                )
                .amount(transaction.getAmount())
                .transactionType(
                        transaction.getTransactionType()
                )
                .transactionStatus(
                        transaction.getTransactionStatus()
                )
                .description(transaction.getDescription())
                .transactionDate(
                        transaction.getTransactionDate()
                )
                .build();
    }
}
