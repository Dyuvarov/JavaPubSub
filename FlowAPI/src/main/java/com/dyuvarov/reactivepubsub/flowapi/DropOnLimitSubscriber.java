package com.dyuvarov.reactivepubsub.flowapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * Subscriber which consumes limited number of elements from publisher.
 * Takes itemsLimit count of items and stops to handle backpressure.
 */
@Log4j2
@RequiredArgsConstructor
public class DropOnLimitSubscriber<T> implements Flow.Subscriber<T>{
    private Flow.Subscription subscription;

    /** How many items will be processed from publisher */
    @NonNull
    private long itemsLimit;

    /** Received item handler */
    private final Consumer<? super T> handler;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.info("request ({})", 1);
        subscription.request(1L);
    }

    @Override
    public void onNext(T item) {
        try {
            handler.accept(item);
        } catch (Throwable throwable) {
            onError(throwable);
        }
        if (--itemsLimit <= 0) {
            log.info("Subscriber dropped by limit");
        } else {
            log.info("request ({})", 1);
            subscription.request(1L);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error!", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Everything received");
    }
}
