package com.dyuvarov.reactivepubsub.reactor;

import com.dyuvarov.reactivepubsub.common.CodeReviewer;
import com.dyuvarov.reactivepubsub.common.Feature;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class Application {
    public void run() {
        Flux<Feature> coder = createCoder();
        BatchSubscriber<Feature> batchReviewer =
                new BatchSubscriber<>(new CodeReviewer("Richard", 200L)::review, 2L);

        DropOnLimitSubscriber<Feature> dropOnLimitSubscriber =
                new DropOnLimitSubscriber<>(new CodeReviewer("Gilfoyle", 400L)::review, 3L);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> coder.subscribe(batchReviewer)),
                CompletableFuture.runAsync(() -> coder.subscribe(dropOnLimitSubscriber))
        ).join();
    }
    private Flux<Feature> createCoder() {
        AtomicInteger featureNum = new AtomicInteger();
        return Flux.generate(synchronousSink -> {
                    synchronousSink.next(new Feature(featureNum.getAndIncrement()));
                })
                .doOnRequest(r-> log.info("request  " + r))
                .cast(Feature.class);
    }
}
