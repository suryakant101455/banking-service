package com.sd.bank.service;

import com.sd.bank.dto.TransactionRequest;
import com.sd.bank.dto.TransactionResponse;
import com.sd.bank.entity.Account;
import com.sd.bank.entity.BankTransaction;
import com.sd.bank.exception.ResourceNotFoundException;
import com.sd.bank.repository.AccountRepository;
import com.sd.bank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BankTransactionService {

    private final BankTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionResponse create(TransactionRequest request) {

        Account account = accountRepository.findById(
                request.getAccountId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Account not found: " + request.getAccountId()
                ));

        BankTransaction transaction = BankTransaction.builder()
                .transactionReference(
                        request.getTransactionReference()
                )
                .account(account)
                .amount(request.getAmount())
                .transactionType(request.getTransactionType())
                .transactionStatus(request.getTransactionStatus())
                .description(request.getDescription())
                .build();

        return mapToResponse(
                transactionRepository.save(transaction)
        );
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(Long id) {

        BankTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found: " + id
                                ));

        return mapToResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAll() {

        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getByAccountId(
            Long accountId
    ) {

        return transactionRepository
                .findByAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TransactionResponse update(
            Long id,
            TransactionRequest request
    ) {

        BankTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found: " + id
                                ));

        transaction.setDescription(request.getDescription());

        if (request.getTransactionStatus() != null) {
            transaction.setTransactionStatus(
                    request.getTransactionStatus()
            );
        }

        return mapToResponse(
                transactionRepository.save(transaction)
        );
    }

    public void delete(Long id) {

        BankTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found: " + id
                                ));

        transactionRepository.delete(transaction);
    }

    private TransactionResponse mapToResponse(
            BankTransaction transaction
    ) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .transactionStatus(
                        transaction.getTransactionStatus()
                )
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
