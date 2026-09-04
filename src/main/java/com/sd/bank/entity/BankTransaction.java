package com.sd.bank.entity;

import com.sd.bank.enums.TransactionStatus;
import com.sd.bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bank_transaction",
        schema = "banking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transaction_reference",
                        columnNames = "transaction_reference"
                )
        },
        indexes = {
                @Index(
                        name = "idx_transaction_account_id",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_transaction_date",
                        columnList = "transaction_date"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transaction_reference",
            nullable = false,
            unique = true,
            length = 100
    )
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_bank_transaction_account"
            )
    )
    private Account account;

    @Column(
            name = "amount",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus transactionStatus =
            TransactionStatus.PENDING;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
    }
}
