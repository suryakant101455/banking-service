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
@Schema(description = "Request for deposit or withdrawal")
public class MoneyRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(
            description = "Amount of money",
            example = "5000.00"
    )
    private BigDecimal amount;

    @Schema(
            description = "Description of the transaction",
            example = "Cash deposit"
    )
    private String description;
}
