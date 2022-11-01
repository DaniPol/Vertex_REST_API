package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import javafx.util.Pair;
import org.example.EventBusTopics;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderVerticle extends AbstractVerticle {
    JSONParser parser = new JSONParser();
    String usersOrderPath = "C:\\Users\\danie\\IdeaProjects\\Vertex\\src\\main\\java\\org\\example\\verticles\\orders.json";
    private MessageConsumer<String> m_addOrderEvent;
    private MessageConsumer<String> m_getOrdersEvent;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        m_addOrderEvent = vertx.eventBus().consumer(EventBusTopics.ADD_ORDER, h->{
            addOrder(h.body());
            h.reply("Order added");
        });
        m_getOrdersEvent = vertx.eventBus().consumer(EventBusTopics.GET_ORDERS, h->{
            String userOrders = getOrder(h.body());
            h.reply(userOrders);
        });
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        m_addOrderEvent.unregister(ar->{
            if(ar.succeeded())
                m_getOrdersEvent.unregister(ar1->{
                    if(ar1.succeeded())
                        stopPromise.complete();
                    else
                        stopPromise.fail(ar1.cause());
                });
            else
                stopPromise.fail(ar.cause());
        });
    }


    public void addOrder(String message) {
        String[] msg = message.split(";");
        String username = msg[0];
        String order = msg[1];

        Object obj = null;

        try {
            obj = new JSONParser().parse(new FileReader(this.usersOrderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = (JSONObject) obj;
        List  userOrders = new ArrayList();

        if(jsonObject.containsKey(username)) {
            userOrders = (List) jsonObject.get(username);
        }
        else{
            userOrders = new ArrayList();
        }

        userOrders.add(order);
        ((JSONObject) obj).put(username, userOrders);

        try (FileWriter file = new FileWriter(this.usersOrderPath)) {
            file.write(obj.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOrder(String username){
        Object obj = null;

        try {
            obj = this.parser.parse(new FileReader(this.usersOrderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = (JSONObject) obj;
        String orders = (String) jsonObject.get(username).toString();

        return orders;
    }
}
