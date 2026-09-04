package com.sd.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Money transfer request")
public class TransferRequest {

    @NotNull
    @Schema(
            description = "Source account ID",
            example = "1"
    )
    private Long sourceAccountId;

    @NotNull
    @Schema(
            description = "Beneficiary ID",
            example = "10"
    )
    private Long beneficiaryId;

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(
            description = "Transfer amount",
            example = "2500.00"
    )
    private BigDecimal amount;

    @Schema(
            description = "Transfer description",
            example = "Rent payment"
    )
    private String description;
}
