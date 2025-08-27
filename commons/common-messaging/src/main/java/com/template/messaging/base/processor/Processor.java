package com.template.messaging.base.processor;

import com.template.messaging.event.Event;

public interface Processor<C extends Event> {
    void process(C event);
}
