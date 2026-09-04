package com.sd.bank.dto;

import com.sd.bank.enums.BeneficiaryStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryRequest {

    private Long customerId;

    private String beneficiaryName;

    private String accountNumber;

    private String ifscCode;

    private String bankName;

    private BeneficiaryStatus status;
}
