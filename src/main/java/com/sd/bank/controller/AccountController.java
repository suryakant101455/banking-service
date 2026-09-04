package com.sd.bank.controller;

import com.sd.bank.dto.AccountRequest;
import com.sd.bank.dto.AccountResponse;
import com.sd.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @RequestBody AccountRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                accountService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAll() {
        return ResponseEntity.ok(
                accountService.getAll()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getByCustomer(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(
                accountService.getByCustomerId(customerId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long id,
            @RequestBody AccountRequest request
    ) {
        return ResponseEntity.ok(
                accountService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        accountService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
