package com.template.starter.outbox.util;

import com.template.messaging.event.Event;

public interface EventClassResolver {
    Class<? extends Event> resolve(String type);
}
