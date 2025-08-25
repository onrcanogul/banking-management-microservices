-- ============================================================
-- ACCOUNT: müşteri hesabı meta verisi (bakiye ledger'dan türetilir)
-- ============================================================

CREATE TABLE ACCOUNT (
                         ID                RAW(16)        NOT NULL,                    -- UUID (app üretir)
                         CUSTOMER_ID       RAW(16)        NOT NULL,                    -- Müşteri referansı
                         LEDGER_ACCOUNT_ID RAW(16)        NOT NULL,                    -- LEDGER.LEDGER_ACCOUNT.ID (FK opsiyonel)
                         IBAN              VARCHAR2(34)   NOT NULL,                    -- Tekil IBAN
                         CURRENCY          CHAR(3)        NOT NULL,                    -- ISO-4217, örn. 'TRY'
                         STATUS            VARCHAR2(20)   DEFAULT 'ACTIVE' NOT NULL,   -- ACTIVE|BLOCKED|CLOSED
                         CREATED_AT        TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                         UPDATED_AT        TIMESTAMP WITH TIME ZONE,
                         ROW_VERSION       NUMBER(19,0),                               -- Optimistic locking (@Version)
                         CONSTRAINT PK_ACCOUNT PRIMARY KEY (ID),
                         CONSTRAINT UK_ACCOUNT_IBAN UNIQUE (IBAN),                     -- ➜ IBAN benzersiz; GET /accounts/{iban} için hızlı erişim.
                         CONSTRAINT CK_ACCOUNT_STATUS CHECK (STATUS IN ('ACTIVE','BLOCKED','CLOSED')), -- ➜ Geçersiz durumları engeller.
                         CONSTRAINT CK_ACCOUNT_CCY CHECK (LENGTH(CURRENCY)=3)          -- ➜ Para birimi tam 3 karakter olmalı.
);

-- Performans amaçlı yardımcı index'ler
CREATE INDEX IX_ACCOUNT_CUSTOMER
    ON ACCOUNT (CUSTOMER_ID);
-- ➜ Bir müşterinin tüm hesaplarını listeleme sorgularını hızlandırır.

CREATE INDEX IX_ACCOUNT_LEDGER_ACC
    ON ACCOUNT (LEDGER_ACCOUNT_ID);
-- ➜ Ledger ile join’lerde ve bakiye view’larında taramayı azaltır.

CREATE INDEX IX_ACCOUNT_CREATED_AT
    ON ACCOUNT (CREATED_AT);
-- ------------------------------------------------------------

-- ============================================================
-- ACCOUNT_LIMIT: hesap bazlı limitler (1:1)
-- ============================================================

CREATE TABLE ACCOUNT_LIMIT (
                               ID             RAW(16)       NOT NULL,
                               ACCOUNT_ID     RAW(16)       NOT NULL,
                               PER_TX_LIMIT   NUMBER(19,4)  NOT NULL,
                               DAILY_LIMIT    NUMBER(19,4)  NOT NULL,
                               MONTHLY_LIMIT  NUMBER(19,4)  NOT NULL,
                               CURRENCY       CHAR(3)       NOT NULL,     -- Limit para birimi
                               EFFECTIVE_FROM TIMESTAMP WITH TIME ZONE,   -- Opsiyonel yürürlük başlangıcı
                               EFFECTIVE_TO   TIMESTAMP WITH TIME ZONE,   -- Opsiyonel yürürlük bitişi
                               ROW_VERSION    NUMBER(19,0),
                               CONSTRAINT PK_ACC_LIMIT PRIMARY KEY (ID),
                               CONSTRAINT UK_ACC_LIMIT_ACCOUNT UNIQUE (ACCOUNT_ID),
                               CONSTRAINT FK_ACC_LIMIT_ACCOUNT FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID),
                               CONSTRAINT CK_ACC_LIMIT_AMTS CHECK (
                                   PER_TX_LIMIT >= 0 AND DAILY_LIMIT >= 0 AND MONTHLY_LIMIT >= 0
                                   ),
                               CONSTRAINT CK_ACC_LIMIT_CCY CHECK (LENGTH(CURRENCY)=3),
                               CONSTRAINT CK_ACC_LIMIT_DATE CHECK (
                                   EFFECTIVE_FROM IS NULL OR EFFECTIVE_TO   IS NULL OR EFFECTIVE_FROM < EFFECTIVE_TO
                                   )
);


-- ------------------------------------------------------------

-- ============================================================
-- ACCOUNT_STATUS_HISTORY: durum değişiklik geçmişi (N:1)
-- ============================================================

CREATE TABLE ACCOUNT_STATUS_HISTORY (
                                        ID          RAW(16)       NOT NULL,
                                        ACCOUNT_ID  RAW(16)       NOT NULL,
                                        OLD_STATUS  VARCHAR2(20)  NOT NULL,
                                        NEW_STATUS  VARCHAR2(20)  NOT NULL,
                                        CHANGED_AT  TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                                        REASON      VARCHAR2(200),
                                        CONSTRAINT PK_ACC_STATUS_H PRIMARY KEY (ID),
                                        CONSTRAINT FK_ACC_STATUS_ACCOUNT FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID),
                                        CONSTRAINT CK_ACC_STATUS_ENUM CHECK (
                                            OLD_STATUS IN ('ACTIVE','BLOCKED','CLOSED')
                                                AND NEW_STATUS IN ('ACTIVE','BLOCKED','CLOSED')
                                            )
);

CREATE INDEX IX_ACC_STATUS_ACCOUNT
    ON ACCOUNT_STATUS_HISTORY (ACCOUNT_ID);
CREATE INDEX IX_ACC_STATUS_ACCOUNT_DT
    ON ACCOUNT_STATUS_HISTORY (ACCOUNT_ID, CHANGED_AT DESC);

-- (Opsiyonel) Dokümantasyon için COMMENT'ler
COMMENT ON TABLE ACCOUNT IS 'Hesap meta verisi; bakiye ledger''dan türetilir.';
COMMENT ON COLUMN ACCOUNT.LEDGER_ACCOUNT_ID IS 'LEDGER.LEDGER_ACCOUNT.ID referansı (FK opsiyonel).';
COMMENT ON TABLE ACCOUNT_LIMIT IS 'Hesap bazlı işlem/gün/ay limitleri.';
COMMENT ON TABLE ACCOUNT_STATUS_HISTORY IS 'Hesap durum değişikliklerinin geçmişi.';
