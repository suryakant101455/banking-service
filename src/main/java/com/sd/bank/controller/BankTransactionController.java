package com.sd.bank.controller;

import com.sd.bank.dto.TransactionRequest;
import com.sd.bank.dto.TransactionResponse;
import com.sd.bank.service.BankTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @RequestBody TransactionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                transactionService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(
                transactionService.getAll()
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getByAccount(
            @PathVariable Long accountId
    ) {
        return ResponseEntity.ok(
                transactionService.getByAccountId(accountId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @RequestBody TransactionRequest request
    ) {
        return ResponseEntity.ok(
                transactionService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        transactionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
