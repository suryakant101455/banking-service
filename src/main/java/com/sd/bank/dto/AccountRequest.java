package com.sd.bank.dto;

import com.sd.bank.enums.AccountStatus;
import com.sd.bank.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    private String accountNumber;

    private Long customerId;

    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private AccountStatus status;
}
