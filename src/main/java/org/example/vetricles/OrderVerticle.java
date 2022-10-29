package org.example.vetricles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.example.EventBusTopics;

public class OrderVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().publish(EventBusTopics.START_EVENT, "OrderVerticle started!");
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }
}
