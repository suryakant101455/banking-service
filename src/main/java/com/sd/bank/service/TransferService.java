package com.sd.bank.service;

import com.sd.bank.dto.TransferRequest;
import com.sd.bank.dto.TransferResponse;
import com.sd.bank.entity.Account;
import com.sd.bank.entity.BankTransaction;
import com.sd.bank.entity.Beneficiary;
import com.sd.bank.enums.AccountStatus;
import com.sd.bank.enums.BeneficiaryStatus;
import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import com.sd.bank.exception.ResourceNotFoundException;
import com.sd.bank.repository.AccountRepository;
import com.sd.bank.repository.BankTransactionRepository;
import com.sd.bank.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final BankTransactionRepository transactionRepository;


    @Transactional
    public TransferResponse transfer(
            TransferRequest request
    ) {

        validateAmount(request.getAmount());


        /*
         * --------------------------------------------------
         * STEP 1
         * Lock source account
         * --------------------------------------------------
         */
        Account sourceAccount =
                accountRepository.findByIdForUpdate(
                        request.getSourceAccountId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source account not found: "
                                        + request.getSourceAccountId()
                        ));


        /*
         * --------------------------------------------------
         * STEP 2
         * Validate source account
         * --------------------------------------------------
         */
        validateAccount(sourceAccount);


        /*
         * --------------------------------------------------
         * STEP 3
         * Validate beneficiary
         *
         * Beneficiary must belong to the customer who owns
         * the source account.
         * --------------------------------------------------
         */
        Beneficiary beneficiary =
                beneficiaryRepository
                        .findActiveBeneficiaryForCustomer(
                                request.getBeneficiaryId(),
                                sourceAccount.getCustomer().getId(),
                                BeneficiaryStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Beneficiary not found or "
                                                + "does not belong to source customer"
                                ));


        /*
         * --------------------------------------------------
         * STEP 4
         * Find destination account
         * --------------------------------------------------
         */
        Account destinationAccount =
                accountRepository
                        .findByAccountNumberForUpdate(
                                beneficiary.getAccountNumber()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destination account not found: "
                                                + beneficiary.getAccountNumber()
                                ));


        /*
         * --------------------------------------------------
         * STEP 5
         * Validate destination account
         * --------------------------------------------------
         */
        validateAccount(destinationAccount);


        /*
         * --------------------------------------------------
         * STEP 6
         * Prevent transferring to same account
         * --------------------------------------------------
         */
        if (sourceAccount.getId()
                .equals(destinationAccount.getId())) {

            throw new IllegalStateException(
                    "Source and destination accounts cannot be same"
            );
        }


        /*
         * --------------------------------------------------
         * STEP 7
         * Check sufficient balance
         * --------------------------------------------------
         */
        BigDecimal amount = request.getAmount();

        if (sourceAccount.getBalance()
                .compareTo(amount) < 0) {

            throw new IllegalStateException(
                    "Insufficient balance. Available: "
                            + sourceAccount.getBalance()
                            + ", Requested: "
                            + amount
            );
        }


        /*
         * --------------------------------------------------
         * STEP 8
         * Debit source account
         * --------------------------------------------------
         */
        sourceAccount.setBalance(
                sourceAccount.getBalance()
                        .subtract(amount)
        );


        /*
         * --------------------------------------------------
         * STEP 9
         * Credit destination account
         * --------------------------------------------------
         */
        destinationAccount.setBalance(
                destinationAccount.getBalance()
                        .add(amount)
        );


        /*
         * --------------------------------------------------
         * STEP 10
         * Generate transaction references
         * --------------------------------------------------
         */
        String transferReference =
                "TXN-" + UUID.randomUUID();


        /*
         * --------------------------------------------------
         * STEP 11
         * Create DEBIT transaction
         * --------------------------------------------------
         */
        BankTransaction debitTransaction =
                BankTransaction.builder()
                        .transactionReference(
                                transferReference + "-D"
                        )
                        .account(sourceAccount)
                        .amount(amount)
                        .transactionType(
                                TransactionType.DEBIT
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .description(
                                request.getDescription() != null
                                        ? request.getDescription()
                                        : "Fund transfer"
                        )
                        .build();


        /*
         * --------------------------------------------------
         * STEP 12
         * Create CREDIT transaction
         * --------------------------------------------------
         */
        BankTransaction creditTransaction =
                BankTransaction.builder()
                        .transactionReference(
                                transferReference + "-C"
                        )
                        .account(destinationAccount)
                        .amount(amount)
                        .transactionType(
                                TransactionType.CREDIT
                        )
                        .transactionStatus(
                                TransactionStatus.SUCCESS
                        )
                        .description(
                                request.getDescription() != null
                                        ? request.getDescription()
                                        : "Fund transfer"
                        )
                        .build();


        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);


        /*
         * --------------------------------------------------
         * STEP 13
         * COMMIT
         *
         * If anything above throws an exception,
         * the entire transaction is rolled back.
         * --------------------------------------------------
         */

        return TransferResponse.builder()
                .transactionReference(transferReference)
                .sourceAccountId(sourceAccount.getId())
                .destinationAccountId(
                        destinationAccount.getId()
                )
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .message("Transfer completed successfully")
                .build();
    }


    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero"
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
}
