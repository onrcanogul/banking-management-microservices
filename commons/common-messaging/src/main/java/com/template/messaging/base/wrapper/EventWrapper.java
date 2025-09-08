package com.template.messaging.base.wrapper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.template.messaging.event.Event;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EventWrapper<T extends Event> (
        UUID id,
        String type,
        String source,
        Instant time,
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.CLASS,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "type"
        )
        T event,
        Map<String, String> headers
) {
}
