CREATE TABLE transfer (
                          id              RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                          from_account_id RAW(16) NOT NULL,
                          to_account_id   RAW(16) NOT NULL,
                          currency        CHAR(3) NOT NULL,
                          amount          NUMBER(19,4) NOT NULL,
                          external_ref    VARCHAR2(100 CHAR),
                          status          VARCHAR2(20 CHAR) NOT NULL,
                          created_at      TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
                          updated_at      TIMESTAMP WITH TIME ZONE,
                          posted_at       TIMESTAMP WITH TIME ZONE,
                          failed_at       TIMESTAMP WITH TIME ZONE,
                          canceled_at     TIMESTAMP WITH TIME ZONE,
                          version         NUMBER(19) DEFAULT 0 NOT NULL,
                          CONSTRAINT ck_transfer_amount CHECK (amount >= 0.01),
                          CONSTRAINT ux_transfer_external_ref UNIQUE (external_ref)
);

CREATE INDEX ix_transfer_status ON transfer(status);
CREATE INDEX ix_transfer_from   ON transfer(from_account_id);
CREATE INDEX ix_transfer_to     ON transfer(to_account_id);


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
