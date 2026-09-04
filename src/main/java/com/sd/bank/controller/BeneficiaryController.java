package com.sd.bank.controller;

import com.sd.bank.dto.BeneficiaryRequest;
import com.sd.bank.dto.BeneficiaryResponse;
import com.sd.bank.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> create(
            @RequestBody BeneficiaryRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(beneficiaryService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                beneficiaryService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAll() {
        return ResponseEntity.ok(
                beneficiaryService.getAll()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BeneficiaryResponse>> getByCustomer(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(
                beneficiaryService.getByCustomerId(customerId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> update(
            @PathVariable Long id,
            @RequestBody BeneficiaryRequest request
    ) {
        return ResponseEntity.ok(
                beneficiaryService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        beneficiaryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
