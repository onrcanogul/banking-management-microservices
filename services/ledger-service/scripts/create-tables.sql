-- =======================
-- LEDGER_ACCOUNT
-- =======================
CREATE TABLE LEDGER_ACCOUNT (
                                ID               RAW(16)       NOT NULL,
                                EXTERNAL_REF_TYPE VARCHAR2(30) NOT NULL,        -- "ACCOUNT"
                                EXTERNAL_REF_ID   VARCHAR2(64) NOT NULL,        -- accountId
                                CURRENCY          CHAR(3)      NOT NULL,        -- ISO-4217
                                STATUS            VARCHAR2(20) DEFAULT 'ACTIVE' NOT NULL, -- ACTIVE|CLOSED
                                CREATED_AT        TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                                IS_DELETED        NUMBER(1) DEFAULT 0 NOT NULL,
                                CONSTRAINT PK_LEDGER_ACCOUNT PRIMARY KEY (ID),
                                CONSTRAINT CK_LA_CCY CHECK (LENGTH(CURRENCY)=3),
                                CONSTRAINT CK_LA_STATUS CHECK (STATUS IN ('ACTIVE','CLOSED')),
                                CONSTRAINT CK_LA_IS_DEL CHECK (IS_DELETED IN (0,1))
);

CREATE UNIQUE INDEX UX_LA_EXT_ACTIVE
    ON LEDGER_ACCOUNT (CASE WHEN IS_DELETED = 0 THEN EXTERNAL_REF_TYPE ELSE NULL END,
                       CASE WHEN IS_DELETED = 0 THEN EXTERNAL_REF_ID   ELSE NULL END,
                       CASE WHEN IS_DELETED = 0 THEN CURRENCY          ELSE NULL END);

CREATE INDEX IX_LA_ACTIVE ON LEDGER_ACCOUNT (IS_DELETED);

-- =======================
-- LEDGER_ENTRY (double-entry)
-- =======================
CREATE TABLE LEDGER_ENTRY (
                              ID             RAW(16)       NOT NULL,
                              DEBIT_ACCOUNT  RAW(16)       NOT NULL,
                              CREDIT_ACCOUNT RAW(16)       NOT NULL,
                              AMOUNT         NUMBER(19,4)  NOT NULL,
                              CURRENCY       CHAR(3)       NOT NULL,
                              REF_TYPE       VARCHAR2(30)  NOT NULL,          -- "TRANSFER" vb.
                              REF_ID         VARCHAR2(64)  NOT NULL,          -- "REF-001"
                              DESCRIPTION    VARCHAR2(200),
                              CREATED_AT     TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                              REVERSAL_OF    RAW(16),
                              CONSTRAINT PK_LEDGER_ENTRY PRIMARY KEY (ID),
                              CONSTRAINT CK_LE_POSITIVE CHECK (AMOUNT > 0),
                              CONSTRAINT CK_LE_CCY CHECK (LENGTH(CURRENCY)=3),
                              CONSTRAINT CK_LE_ACCTS CHECK (DEBIT_ACCOUNT <> CREDIT_ACCOUNT)
);

CREATE UNIQUE INDEX UX_LE_UNIQ_REF ON LEDGER_ENTRY (REF_TYPE, REF_ID);

CREATE INDEX IX_LE_DEBIT_DT   ON LEDGER_ENTRY (DEBIT_ACCOUNT,  CREATED_AT);
CREATE INDEX IX_LE_CREDIT_DT  ON LEDGER_ENTRY (CREDIT_ACCOUNT, CREATED_AT);
CREATE INDEX IX_LE_CREATED_DT ON LEDGER_ENTRY (CREATED_AT);

CREATE OR REPLACE VIEW LEDGER_BALANCE_V AS
SELECT la.ID AS LEDGER_ACCOUNT_ID,
       la.CURRENCY,
       NVL((
               SELECT SUM(
                              CASE
                                  WHEN le.CREDIT_ACCOUNT = la.ID THEN le.AMOUNT
                                  WHEN le.DEBIT_ACCOUNT  = la.ID THEN -le.AMOUNT
                                  END)
               FROM LEDGER_ENTRY le
               WHERE le.CURRENCY = la.CURRENCY
           ), 0) AS BALANCE
FROM LEDGER_ACCOUNT la
WHERE la.IS_DELETED = 0;
