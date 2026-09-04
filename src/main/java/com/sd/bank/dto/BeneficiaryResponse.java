package com.sd.bank.dto;

import com.sd.bank.enums.BeneficiaryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {

    private Long id;

    private Long customerId;

    private String beneficiaryName;

    private String accountNumber;

    private String ifscCode;

    private String bankName;

    private BeneficiaryStatus status;

    private LocalDateTime createdAt;
}
