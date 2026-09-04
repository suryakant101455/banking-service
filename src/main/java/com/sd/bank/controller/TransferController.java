package com.sd.bank.controller;

import com.sd.bank.dto.TransferRequest;
import com.sd.bank.dto.TransferResponse;
import com.sd.bank.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(
        name = "Transfers",
        description = "Bank-to-bank fund transfer APIs"
)
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(
            summary = "Transfer money",
            description = """
                    Transfers money from a source account
                    to a beneficiary account.
                    """
    )
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

        TransferResponse response =
                transferService.transfer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
