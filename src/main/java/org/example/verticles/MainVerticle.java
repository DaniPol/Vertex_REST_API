package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import org.example.EventBusTopics;

public class MainVerticle extends AbstractVerticle {

    private MessageConsumer<String> m_startEvent;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        m_startEvent = vertx.eventBus().consumer(EventBusTopics.START_EVENT, h->{
            System.out.println("MainVerticel====> "+h.body());
        });

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        m_startEvent.unregister(ar->{
            if(ar.succeeded())
                stopPromise.complete();
            else
                stopPromise.fail(ar.cause());
        });
    }
}
