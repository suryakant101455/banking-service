package com.sd.bank.dto;

import com.sd.bank.enums.AccountStatus;
import com.sd.bank.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private Long customerId;

    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
