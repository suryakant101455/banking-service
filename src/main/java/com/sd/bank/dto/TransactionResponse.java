package com.sd.bank.dto;

import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;

    private String transactionReference;

    private Long accountId;

    private BigDecimal amount;

    private TransactionType transactionType;

    private TransactionStatus transactionStatus;

    private String description;

    private LocalDateTime transactionDate;
}
