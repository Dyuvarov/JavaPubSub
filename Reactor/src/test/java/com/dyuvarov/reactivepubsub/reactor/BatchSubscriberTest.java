package com.dyuvarov.reactivepubsub.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchSubscriberTest {

    @Test
    public void shoudRequestDependOnBatchSize() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Flux<Integer> publisher = Flux.fromArray(new Integer[]{1,2,3,4,5,6}).doOnRequest(r -> atomicInteger.incrementAndGet());
        BatchSubscriber<Integer> subscriber = new BatchSubscriber<>(x -> {}, 3L);

        publisher.subscribe(subscriber);
        //3 requests: 1 got (1,2,3), 2 got (4,5,6), 3 request next values if exists
        assertEquals(3, atomicInteger.get());
    }

    @Test
    public void shouldConsumeAllItems() {
        Integer[] publishedItems = new Integer[]{1,2,3,4,5,6};
        Flux<Integer> publisher =Flux.fromArray(publishedItems);
        List<Integer> consumedItems = new ArrayList<>(publishedItems.length);
        BatchSubscriber<Integer> subscriber = new BatchSubscriber<>(consumedItems::add, 3);

        publisher.subscribe(subscriber);
        assertEquals(publishedItems.length, consumedItems.size());
    }
}
