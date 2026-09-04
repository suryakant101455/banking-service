package com.sd.bank.repository;


import com.sd.bank.entity.Beneficiary;
import com.sd.bank.enums.BeneficiaryStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository
        extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByCustomerId(Long customerId);

    List<Beneficiary> findByCustomerIdAndStatus(
            Long customerId,
            BeneficiaryStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
            SELECT b
            FROM Beneficiary b
            WHERE b.id = :beneficiaryId
              AND b.customer.id = :customerId
              AND b.status = :status
            """)
    Optional<Beneficiary> findActiveBeneficiaryForCustomer(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("customerId") Long customerId,
            @Param("status") BeneficiaryStatus status
    );
}
