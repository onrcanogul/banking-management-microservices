package com.template.core.audit;

import java.time.Instant;
import java.time.OffsetDateTime;

/**
 * Marker + contract for update auditing.
 * Entities implementing this must expose updatedAt/updatedBy fields.
 */
public interface IUpdateAuditing {
    OffsetDateTime getUpdatedAt();
    void setUpdatedAt(OffsetDateTime updatedAt);
}
