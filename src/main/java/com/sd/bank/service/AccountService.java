package com.sd.bank.service;

import com.sd.bank.dto.AccountRequest;
import com.sd.bank.dto.AccountResponse;
import com.sd.bank.entity.Account;
import com.sd.bank.entity.Customer;
import com.sd.bank.exception.ResourceNotFoundException;
import com.sd.bank.repository.AccountRepository;
import com.sd.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountResponse create(AccountRequest request) {

        Customer customer = customerRepository.findById(
                request.getCustomerId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Customer not found: " + request.getCustomerId()
                ));

        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .customer(customer)
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .currency(
                        request.getCurrency() != null
                                ? request.getCurrency()
                                : "INR"
                )
                .status(request.getStatus())
                .build();

        return mapToResponse(accountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public AccountResponse getById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));

        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAll() {

        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getByCustomerId(
            Long customerId
    ) {

        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AccountResponse update(
            Long id,
            AccountRequest request
    ) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));

        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());

        if (request.getStatus() != null) {
            account.setStatus(request.getStatus());
        }

        return mapToResponse(accountRepository.save(account));
    }

    public void delete(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));

        accountRepository.delete(account);
    }

    private AccountResponse mapToResponse(Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomer().getId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
