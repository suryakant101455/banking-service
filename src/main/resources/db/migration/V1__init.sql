--===========
-- Create schema
CREATE SCHEMA IF NOT EXISTS banking;
-- =========================================================
-- 1. CUSTOMER
-- =========================================================

CREATE TABLE customer (
                          id              BIGSERIAL PRIMARY KEY,
                          customer_number VARCHAR(50)  NOT NULL UNIQUE,
                          first_name      VARCHAR(100) NOT NULL,
                          last_name       VARCHAR(100) NOT NULL,
                          email           VARCHAR(255) NOT NULL UNIQUE,
                          phone           VARCHAR(20),
                          date_of_birth   DATE,
                          status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
                          created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT chk_customer_status
                              CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED'))
);


-- =========================================================
-- 2. ACCOUNT
-- =========================================================

CREATE TABLE account (
                         id              BIGSERIAL PRIMARY KEY,
                         account_number  VARCHAR(30)    NOT NULL UNIQUE,
                         customer_id     BIGINT         NOT NULL,
                         account_type    VARCHAR(20)    NOT NULL,
                         balance         NUMERIC(19, 4) NOT NULL DEFAULT 0,
                         currency        VARCHAR(3)     NOT NULL DEFAULT 'INR',
                         status          VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
                         created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_account_customer
                             FOREIGN KEY (customer_id)
                                 REFERENCES customer(id),

                         CONSTRAINT chk_account_type
                             CHECK (account_type IN ('SAVINGS', 'CURRENT')),

                         CONSTRAINT chk_account_status
                             CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),

                         CONSTRAINT chk_account_balance
                             CHECK (balance >= 0)
);


-- =========================================================
-- 3. TRANSACTION
-- =========================================================

CREATE TABLE bank_transaction (
                                  id                    BIGSERIAL PRIMARY KEY,
                                  transaction_reference VARCHAR(100)   NOT NULL UNIQUE,
                                  account_id            BIGINT         NOT NULL,
                                  amount                NUMERIC(19, 4) NOT NULL,
                                  transaction_type      VARCHAR(20)    NOT NULL,
                                  transaction_status    VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
                                  description           VARCHAR(500),
                                  transaction_date      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_bank_transaction_account
                                      FOREIGN KEY (account_id)
                                          REFERENCES account(id),

                                  CONSTRAINT chk_bank_transaction_amount
                                      CHECK (amount > 0),

                                  CONSTRAINT chk_bank_transaction_type
                                      CHECK (transaction_type IN ('CREDIT', 'DEBIT')),

                                  CONSTRAINT chk_bank_transaction_status
                                      CHECK (transaction_status IN ('PENDING', 'SUCCESS', 'FAILED', 'REVERSED'))
);


-- =========================================================
-- 4. BENEFICIARY
-- =========================================================

CREATE TABLE beneficiary (
                             id                 BIGSERIAL PRIMARY KEY,
                             customer_id        BIGINT       NOT NULL,
                             beneficiary_name   VARCHAR(150) NOT NULL,
                             account_number     VARCHAR(30)  NOT NULL,
                             ifsc_code          VARCHAR(20),
                             bank_name          VARCHAR(150),
                             status             VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
                             created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_beneficiary_customer
                                 FOREIGN KEY (customer_id)
                                     REFERENCES customer(id),

                             CONSTRAINT chk_beneficiary_status
                                 CHECK (status IN ('ACTIVE', 'INACTIVE'))
);


-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_account_customer_id
    ON account(customer_id);

CREATE INDEX idx_transaction_account_id
    ON bank_transaction(account_id);

CREATE INDEX idx_transaction_date
    ON bank_transaction(transaction_date);

CREATE INDEX idx_beneficiary_customer_id
    ON beneficiary(customer_id);