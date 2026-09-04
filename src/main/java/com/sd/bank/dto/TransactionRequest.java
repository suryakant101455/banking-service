package com.sd.bank.dto;

import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private String transactionReference;

    private Long accountId;

    private BigDecimal amount;

    private TransactionType transactionType;

    private TransactionStatus transactionStatus;

    private String description;
}
