package com.sd.bank.service;

import com.sd.bank.dto.BeneficiaryRequest;
import com.sd.bank.dto.BeneficiaryResponse;
import com.sd.bank.entity.Beneficiary;
import com.sd.bank.entity.Customer;
import com.sd.bank.exception.ResourceNotFoundException;
import com.sd.bank.repository.BeneficiaryRepository;
import com.sd.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;

    public BeneficiaryResponse create(
            BeneficiaryRequest request
    ) {

        Customer customer = customerRepository.findById(
                request.getCustomerId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Customer not found: "
                                + request.getCustomerId()
                ));

        Beneficiary beneficiary = Beneficiary.builder()
                .customer(customer)
                .beneficiaryName(request.getBeneficiaryName())
                .accountNumber(request.getAccountNumber())
                .ifscCode(request.getIfscCode())
                .bankName(request.getBankName())
                .status(request.getStatus())
                .build();

        return mapToResponse(
                beneficiaryRepository.save(beneficiary)
        );
    }

    @Transactional(readOnly = true)
    public BeneficiaryResponse getById(Long id) {

        Beneficiary beneficiary =
                beneficiaryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Beneficiary not found: " + id
                                ));

        return mapToResponse(beneficiary);
    }

    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getAll() {

        return beneficiaryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getByCustomerId(
            Long customerId
    ) {

        return beneficiaryRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BeneficiaryResponse update(
            Long id,
            BeneficiaryRequest request
    ) {

        Beneficiary beneficiary =
                beneficiaryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Beneficiary not found: " + id
                                ));

        beneficiary.setBeneficiaryName(
                request.getBeneficiaryName()
        );
        beneficiary.setAccountNumber(
                request.getAccountNumber()
        );
        beneficiary.setIfscCode(request.getIfscCode());
        beneficiary.setBankName(request.getBankName());

        if (request.getStatus() != null) {
            beneficiary.setStatus(request.getStatus());
        }

        return mapToResponse(
                beneficiaryRepository.save(beneficiary)
        );
    }

    public void delete(Long id) {

        Beneficiary beneficiary =
                beneficiaryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Beneficiary not found: " + id
                                ));

        beneficiaryRepository.delete(beneficiary);
    }

    private BeneficiaryResponse mapToResponse(
            Beneficiary beneficiary
    ) {

        return BeneficiaryResponse.builder()
                .id(beneficiary.getId())
                .customerId(
                        beneficiary.getCustomer().getId()
                )
                .beneficiaryName(
                        beneficiary.getBeneficiaryName()
                )
                .accountNumber(
                        beneficiary.getAccountNumber()
                )
                .ifscCode(beneficiary.getIfscCode())
                .bankName(beneficiary.getBankName())
                .status(beneficiary.getStatus())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }
}
