package com.dyuvarov.reactivepubsub.reactor;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.function.Consumer;

/**
 * Subscriber which consumes limited number of elements from publisher.
 * Takes itemsLimit count of items and stops to handle backpressure.
 */
@Log4j2
@AllArgsConstructor
public class DropOnLimitSubscriber<T> extends BaseSubscriber<T> {
    /** Received item handler */
    private final Consumer<? super T> handler;

    /** How many items will be processed from publisher */
    private long itemsLimit;

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        request(1L);
    }

    @Override
    protected void hookOnCancel() {
        log.info("Subscriber dropped by limit");
    }

    @Override
    protected void hookOnNext(T value) {
        try {
            handler.accept(value);
        } catch (Throwable throwable) {
            onError(throwable);
        }
        if (--itemsLimit <= 0) {
            cancel();
        } else {
            request(1L);
        }
    }
}
