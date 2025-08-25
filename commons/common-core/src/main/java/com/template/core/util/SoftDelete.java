package com.template.core.util;

import com.template.core.audit.ISoftDelete;

import java.time.Instant;

/** Small helpers to mark/unmark soft deletion. */
public final class SoftDelete {
    private SoftDelete() {}

    public static <T extends ISoftDelete> T markDeleted(T entity, String by) {
        entity.setDeleted(true);
        return entity;
    }

    public static <T extends ISoftDelete> T restore(T entity) {
        entity.setDeleted(false);
        return entity;
    }

    public static boolean isActive(ISoftDelete e) {
        return e != null && !e.isDeleted();
    }
}
