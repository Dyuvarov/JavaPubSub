package com.dyuvarov.reactivepubsub.flowapi;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DropOnLimitSubscriberTest {
    @Test
    public void shouldConsumeLimitedItems() {
        int limit = 2;
        List<Integer> publishedItems = List.of(1,2,3,4,5,6);
        DummyPublisher<Integer> publisher = new DummyPublisher<>(publishedItems.iterator());
        List<Integer> consumedItems = new ArrayList<>(publishedItems.size());
        DropOnLimitSubscriber<Integer> subscriber = new DropOnLimitSubscriber<>(limit, consumedItems::add);

        publisher.subscribe(subscriber);
        assertEquals(limit, consumedItems.size());
    }
}
