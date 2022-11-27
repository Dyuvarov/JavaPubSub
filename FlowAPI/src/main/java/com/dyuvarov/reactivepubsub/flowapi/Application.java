package com.dyuvarov.reactivepubsub.flowapi;

import com.dyuvarov.reactivepubsub.common.Feature;
import com.dyuvarov.reactivepubsub.common.CodeReviewer;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.SubmissionPublisher;

@Log4j2
public class Application {
    public void run() throws InterruptedException {
        SubmissionPublisher<Feature> pub = new SubmissionPublisher<>();
        pub.subscribe(new BatchSubscriber<>( 2, new CodeReviewer("Richard", 200L)::review));
        pub.subscribe(new DropOnLimitSubscriber<>(5, new CodeReviewer("Gilfoyle", 400L)::review));

        for (int i = 0; ; i++) {
            Feature feature = new Feature(i);
            pub.submit(feature);
            Thread.sleep(100L);
        }
    }
}
