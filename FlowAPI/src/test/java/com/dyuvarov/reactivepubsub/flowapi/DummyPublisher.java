package com.dyuvarov.reactivepubsub.flowapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;

public class DummyPublisher<T> implements Flow.Publisher<T>{

    Iterator<T> iterator;

    DummyPublisher(Iterator<T> iterator) {
        this.iterator = iterator;
    }
    public List<DummySubscription> subscriptionList = new ArrayList<>();

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        DummySubscription subscription = new DummySubscription(subscriber);
        subscriptionList.add(subscription);
        subscriber.onSubscribe(subscription);
    }

    class DummySubscription implements Flow.Subscription {

        private final Flow.Subscriber<? super T> subscriber;

        public int requestCount = 0;

        public DummySubscription(Flow.Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            ++requestCount;
            for (long demand = n; demand > 0 && iterator.hasNext(); demand--) {
                subscriber.onNext(iterator.next());
            }
        }

        @Override
        public void cancel() {
        }
    }
}
