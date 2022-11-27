package com.dyuvarov.reactivepubsub.reactor;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.function.Consumer;
@Log4j2
@AllArgsConstructor
public class DropOnLimitSubscriber<T> extends BaseSubscriber<T> {

    private final Consumer<? super T> handler;

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
