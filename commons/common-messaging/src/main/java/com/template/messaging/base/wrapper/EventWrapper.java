package com.template.messaging.base.wrapper;

import com.template.messaging.event.Event;

import java.time.Instant;
import java.util.Map;

public record EventWrapper<T extends Event> (
        String id,
        String type,
        String source,
        Instant time,
        T event,
        Map<String, String> headers
) {
}
