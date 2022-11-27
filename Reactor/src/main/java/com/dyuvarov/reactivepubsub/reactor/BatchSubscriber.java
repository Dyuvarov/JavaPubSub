package com.dyuvarov.reactivepubsub.reactor;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.function.Consumer;

/**
 * Subscriber which consumes all elements from publisher.
 * Request as many items as it can process to handle backpressure
 */
@RequiredArgsConstructor
public class BatchSubscriber<T> extends BaseSubscriber<T> {
    /** Items processed since last request. When itemsProcessed==batchSize sends new request */
    private int itemsProcessed = 0;

    /** Received item handler */
    private final Consumer<? super T> handler;

    /** How many items will be requested in each request*/
    private final long batchSize;

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        request(batchSize);
    }

    @Override
    protected void hookOnNext(T value) {
        try {
            handler.accept(value);
        } catch (Exception e) {
            onError(e);
        }

        if (++itemsProcessed >= batchSize) {
            itemsProcessed = 0;
            request(batchSize);
        }
    }
}
