package com.template.messaging.transaction;

import com.template.messaging.base.consumer.Consumer;
import com.template.messaging.base.processor.Processor;
import com.template.messaging.base.wrapper.EventWrapper;
import com.template.messaging.event.Event;

public interface SagaStep<P extends Event, R extends Event> extends Processor<P>, Consumer<R> {
    void process(P event);
    void consume(EventWrapper<R> payload);
}
