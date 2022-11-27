package com.dyuvarov.reactivepubsub.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class CodeReviewer {
    private final String name;
    private final long reviewTime;

    public void review(Feature feature) {
        try {
            Thread.sleep(reviewTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info(String.format("%s: approved %s", name, feature));
    }
}
