-- ============================================================
-- ACCOUNT
-- ============================================================
CREATE TABLE ACCOUNT (
                         ID                RAW(16)        NOT NULL,
                         CUSTOMER_ID       RAW(16)        NOT NULL,
                         LEDGER_ACCOUNT_ID RAW(16),
                         IBAN              VARCHAR2(34)   NOT NULL,
                         CURRENCY          CHAR(3)        NOT NULL,
                         STATUS            VARCHAR2(20)   DEFAULT 'ACTIVE' NOT NULL,
                         CREATED_AT        TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                         UPDATED_AT        TIMESTAMP WITH TIME ZONE,
                         ROW_VERSION       NUMBER(19,0),
                         IS_DELETED        NUMBER(1) DEFAULT 0 NOT NULL,
                         DELETED_AT        TIMESTAMP WITH TIME ZONE,
                         DELETED_BY        VARCHAR2(100),
                         CONSTRAINT PK_ACCOUNT PRIMARY KEY (ID),
                         CONSTRAINT UK_ACCOUNT_IBAN UNIQUE (IBAN),
                         CONSTRAINT CK_ACCOUNT_STATUS CHECK (STATUS IN ('ACTIVE','BLOCKED','CLOSED')),
                         CONSTRAINT CK_ACCOUNT_CCY CHECK (LENGTH(CURRENCY)=3),
                         CONSTRAINT CK_ACCOUNT_IS_DEL CHECK (IS_DELETED IN (0,1))
);

CREATE INDEX IX_ACCOUNT_CUSTOMER     ON ACCOUNT (CUSTOMER_ID);
CREATE INDEX IX_ACCOUNT_LEDGER_ACC   ON ACCOUNT (LEDGER_ACCOUNT_ID);
CREATE INDEX IX_ACCOUNT_CREATED_AT   ON ACCOUNT (CREATED_AT);
CREATE INDEX IX_ACCOUNT_ACTIVE       ON ACCOUNT (IS_DELETED);


COMMENT ON TABLE ACCOUNT IS 'Hesap meta verisi; bakiye ledger''dan türetilir.';
COMMENT ON COLUMN ACCOUNT.LEDGER_ACCOUNT_ID IS 'LEDGER.LEDGER_ACCOUNT.ID referansı (FK opsiyonel).';

-- ============================================================
-- ACCOUNT_LIMIT
-- ============================================================
CREATE TABLE ACCOUNT_LIMIT (
                               ID             RAW(16)       NOT NULL,
                               ACCOUNT_ID     RAW(16)       NOT NULL,
                               PER_TX_LIMIT   NUMBER(19,4)  NOT NULL,
                               DAILY_LIMIT    NUMBER(19,4)  NOT NULL,
                               MONTHLY_LIMIT  NUMBER(19,4)  NOT NULL,
                               CURRENCY       CHAR(3)       NOT NULL,
                               EFFECTIVE_FROM TIMESTAMP WITH TIME ZONE,
                               EFFECTIVE_TO   TIMESTAMP WITH TIME ZONE,
                               ROW_VERSION    NUMBER(19,0),
                               IS_DELETED     NUMBER(1) DEFAULT 0 NOT NULL,
                               DELETED_AT     TIMESTAMP WITH TIME ZONE,
                               DELETED_BY     VARCHAR2(100),
                               CONSTRAINT PK_ACC_LIMIT PRIMARY KEY (ID),
                               CONSTRAINT CK_ACC_LIMIT_AMTS CHECK (PER_TX_LIMIT >= 0 AND DAILY_LIMIT >= 0 AND MONTHLY_LIMIT >= 0),
                               CONSTRAINT CK_ACC_LIMIT_CCY CHECK (LENGTH(CURRENCY)=3),
                               CONSTRAINT CK_ACC_LIMIT_DATE CHECK (EFFECTIVE_FROM IS NULL OR EFFECTIVE_TO IS NULL OR EFFECTIVE_FROM < EFFECTIVE_TO),
                               CONSTRAINT CK_ACC_LIMIT_IS_DEL CHECK (IS_DELETED IN (0,1)),
                               CONSTRAINT FK_ACC_LIMIT_ACCOUNT FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID)
);

CREATE UNIQUE INDEX UX_ACC_LIMIT_ACCOUNT_ACTIVE
    ON ACCOUNT_LIMIT (CASE WHEN IS_DELETED = 0 THEN ACCOUNT_ID ELSE NULL END);

CREATE INDEX IX_ACC_LIMIT_ACTIVE ON ACCOUNT_LIMIT (IS_DELETED);

COMMENT ON TABLE ACCOUNT_LIMIT IS 'Hesap bazlı işlem/gün/ay limitleri.';

-- ============================================================
-- ACCOUNT_STATUS_HISTORY
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
                                            OLD_STATUS IN ('ACTIVE','BLOCKED','CLOSED') AND NEW_STATUS IN ('ACTIVE','BLOCKED','CLOSED')
                                            ),
                                        CONSTRAINT CK_ACC_STATUS_IS_DEL CHECK (IS_DELETED IN (0,1))
);

CREATE INDEX IX_ACC_STATUS_ACCOUNT    ON ACCOUNT_STATUS_HISTORY (ACCOUNT_ID);
CREATE INDEX IX_ACC_STATUS_ACCOUNT_DT ON ACCOUNT_STATUS_HISTORY (ACCOUNT_ID, CHANGED_AT DESC);
CREATE INDEX IX_ACC_STATUS_ACTIVE     ON ACCOUNT_STATUS_HISTORY (IS_DELETED);

COMMENT ON TABLE ACCOUNT_STATUS_HISTORY IS 'Hesap durum değişikliklerinin geçmişi.';


-- ======================
-- OUTBOX TABLE
-- ======================
CREATE TABLE OUTBOX (
                        ID              RAW(16) DEFAULT SYS_GUID() NOT NULL,
                        AGGREGATETYPE   VARCHAR2(255 CHAR),
                        AGGREGATEID     VARCHAR2(255 CHAR),
                        TYPE            VARCHAR2(255 CHAR),
                        PAYLOAD         CLOB,
                        DESTINATION     VARCHAR2(255 CHAR),
                        IS_PUBLISHED    NUMBER(1,0) DEFAULT 0 NOT NULL,
                        CREATED_AT      TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,

                        CONSTRAINT PK_OUTBOX PRIMARY KEY (ID),
                        CONSTRAINT CK_OUTBOX_IS_PUBLISHED CHECK (IS_PUBLISHED IN (0,1))
);

CREATE INDEX IX_IS_PUBLISHED ON OUTBOX (IS_PUBLISHED);

ALTER TABLE outbox ADD dtype VARCHAR2(31);


-- ======================
-- INBOX TABLE
-- ======================

CREATE TABLE INBOX (
            IDEMPOTENT_TOKEN RAW(16) NOT NULL,
            PAYLOAD CLOB,
            TYPE VARCHAR2(255 CHAR),
            IS_PROCESSED NUMBER(1,0) DEFAULT 0 NOT NULL,
            RECEIVED_AT TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,

            CONSTRAINT PK_INBOX PRIMARY KEY (IDEMPOTENT_TOKEN),
            CONSTRAINT CK_INBOX_IS_PROCESSED CHECH (IS_PROCESSED IN (0, 1))
);

CREATE INDEX IX_IS_PROCESSED ON OUTBOX (IS_PROCESSED);
