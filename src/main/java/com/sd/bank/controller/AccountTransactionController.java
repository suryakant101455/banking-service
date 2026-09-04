package com.sd.bank.controller;

import com.sd.bank.dto.MoneyRequest;
import com.sd.bank.dto.TransactionResponse;
import com.sd.bank.service.AccountTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(
        name = "Account Transactions",
        description = "Deposit and withdrawal operations"
)
public class AccountTransactionController {

    private final AccountTransactionService transactionService;


    @PostMapping("/{accountId}/deposit")
    @Operation(
            summary = "Deposit money",
            description = "Deposits money into an active account"
    )
    public ResponseEntity<TransactionResponse> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody MoneyRequest request
    ) {

        TransactionResponse response =
                transactionService.deposit(
                        accountId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/{accountId}/withdraw")
    @Operation(
            summary = "Withdraw money",
            description = "Withdraws money from an active account"
    )
    public ResponseEntity<TransactionResponse> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody MoneyRequest request
    ) {

        TransactionResponse response =
                transactionService.withdraw(
                        accountId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
