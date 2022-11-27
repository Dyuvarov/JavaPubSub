package com.dyuvarov.reactivepubsub.flowapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * Subscriber which consumes all elements from publisher.
 * Request as many items as it can process to handle backpressure
 */
@Log4j2
@RequiredArgsConstructor
public class BatchSubscriber<T> implements Flow.Subscriber<T>{
    private Flow.Subscription subscription;

    /** Items processed since last request. When itemsProcessed==batchSize sends new request */
    private int itemsProcessed = 0;

    /** How many items will be requested in each request*/
    private final long batchSize;

    /** Received item handler */
    private final Consumer<? super T> handler;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.info("request ({})", batchSize);
        subscription.request(batchSize);
    }

    @Override
    public void onNext(T item) {
        try {
            handler.accept(item);
        } catch (Throwable throwable) {
            onError(throwable);
        }

        if (++itemsProcessed >= batchSize) {
            itemsProcessed = 0;
            log.info("request ({})", batchSize);
            subscription.request(batchSize);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error: ", throwable);
    }

    @Override
    public void onComplete() {
        log.info("subscriber completed");
    }
}
