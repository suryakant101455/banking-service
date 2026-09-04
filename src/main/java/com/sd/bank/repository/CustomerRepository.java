package com.sd.bank.repository;
import com.sd.bank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerNumber(String customerNumber);

    Optional<Customer> findByEmail(String email);

    boolean existsByCustomerNumber(String customerNumber);

    boolean existsByEmail(String email);
}
