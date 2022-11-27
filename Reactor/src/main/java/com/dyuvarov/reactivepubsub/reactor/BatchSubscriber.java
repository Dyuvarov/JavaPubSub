package com.dyuvarov.reactivepubsub.reactor;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.function.Consumer;
@RequiredArgsConstructor
public class BatchSubscriber<T> extends BaseSubscriber<T> {
    private int itemsProcessed = 0;
    private final Consumer<? super T> handler;
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
