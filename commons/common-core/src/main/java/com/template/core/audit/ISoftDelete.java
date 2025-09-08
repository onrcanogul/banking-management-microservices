package com.template.core.audit;


/**
 * Marker + contract for soft delete semantics.
 * Entities implementing this must expose deleted flags/metadata.
 */
public interface ISoftDelete {
    boolean isDeleted();
    void setDeleted(boolean deleted);
}