package com.sd.bank.dto;


import com.sd.bank.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {

    private String transactionReference;

    private Long sourceAccountId;

    private Long destinationAccountId;

    private BigDecimal amount;

    private TransactionStatus status;

    private String message;
}
