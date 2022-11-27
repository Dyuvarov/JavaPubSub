package com.dyuvarov.reactivepubsub.flowapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

@Log4j2
@RequiredArgsConstructor
public class BatchSubscriber<T> implements Flow.Subscriber<T>{
    private Flow.Subscription subscription;

    private int itemsProcessed = 0;

    private final long batchSize;

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
