package com.dyuvarov.reactivepubsub.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DropOnLimitSubscriberTest {
    @Test
    public void shouldConsumeLimitedItems() {
        int limit = 2;
        List<Integer> publishedItems = List.of(1,2,3,4,5,6);
        Flux<Integer> publisher = Flux.fromIterable(publishedItems);
        List<Integer> consumedItems = new ArrayList<>(publishedItems.size());
        DropOnLimitSubscriber<Integer> subscriber = new DropOnLimitSubscriber<>(consumedItems::add, limit);

        publisher.subscribe(subscriber);
        assertEquals(limit, consumedItems.size());
    }
}
