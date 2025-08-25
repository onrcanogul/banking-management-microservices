package com.template.core.audit;

import java.time.Instant;
import java.time.OffsetDateTime;

/**
 * Marker + contract for insert auditing.
 * Entities implementing this must expose createdAt/createdBy fields.
 */
public interface IInsertAuditing {
    OffsetDateTime getCreatedAt();
    void setCreatedAt(OffsetDateTime createdAt);
}
