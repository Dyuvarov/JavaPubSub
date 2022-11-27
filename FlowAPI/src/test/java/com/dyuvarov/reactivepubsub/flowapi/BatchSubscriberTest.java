package com.dyuvarov.reactivepubsub.flowapi;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchSubscriberTest {

    @Test
    public void shoudRequestDependOnBatchSize() {
        DummyPublisher<Integer> publisher = new DummyPublisher<>(IntStream.of(1,2,3,4,5,6).iterator());
        BatchSubscriber<Integer> subscriber = new BatchSubscriber<>(3, x -> {});

        publisher.subscribe(subscriber);
        //3 requests: 1 got (1,2,3), 2 got (4,5,6), 3 request next values if exists
        assertEquals(3, publisher.subscriptionList.get(0).requestCount);
    }

    @Test
    public void shouldConsumeAllItems() {
        List<Integer> publishedItems = List.of(1,2,3,4,5,6);
        DummyPublisher<Integer> publisher = new DummyPublisher<>(publishedItems.iterator());
        List<Integer> consumedItems = new ArrayList<>(publishedItems.size());
        BatchSubscriber<Integer> subscriber = new BatchSubscriber<>(3, consumedItems::add);

        publisher.subscribe(subscriber);
        assertEquals(publishedItems.size(), consumedItems.size());
    }
}
