package com.template.messaging.base.consumer;

import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.Event;

public interface Consumer<C extends Event> {
    void consume(EventWrapper<C> payload);
}
