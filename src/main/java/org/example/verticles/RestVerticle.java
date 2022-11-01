package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.EventBusTopics;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import io.vertx.ext.web.Router;

import java.util.HashMap; // import the HashMap class

import javafx.util.Pair;

public class RestVerticle extends AbstractVerticle {
    JSONParser parser = new JSONParser();
    String usersDataPath = "C:\\Users\\danie\\IdeaProjects\\Vertex\\src\\main\\java\\org\\example\\verticles\\users.json";
    int start_port_num = 8080, curr_port_num = 8080;
    HashMap<String, Pair<Integer, HttpServer>> usersPort = new HashMap<String, Pair<Integer, HttpServer>>();


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        //vertx.eventBus().publish(EventBusTopics.START_EVENT, "RestVerticle started!");

        Router router = Router.router(vertx);

        router.get("/Login/:username/:password").handler(ctx->{
            String username = ctx.pathParam("username");
            String password = ctx.pathParam("password");

            if(usersPort.containsKey(username)){
                ctx.request().response().end(String.format("You are already logged in as %s and your port number is %d",username, usersPort.get(username).getKey()));
            }

            else if (Login(username, password)) {
                curr_port_num += 1;
                ctx.request().response().end(String.format("You are logged as %s and your port number is %d",username,curr_port_num));
                HttpServer server =  vertx.createHttpServer();
                usersPort.put(username,new Pair<>(curr_port_num, server) );
                server.requestHandler(router).listen(curr_port_num);
            }

            else
                ctx.request().response().end(String.format("Wrong username or password"));
        });

        router.get("/Login/:username").handler(ctx-> {
            String username = ctx.pathParam("username");

            if(usersPort.containsKey(username))
                ctx.request().response().end(String.format("hi %s ",username));
            else
                ctx.request().response().end(String.format("you are not logged in"));
        });

        router.get("/Logout/:username").handler(ctx-> {
            String username = ctx.pathParam("username");

            if(!Logout(username))
                ctx.request().response().end(String.format("you are not logged in"));
        });

        router.get("/AddOrder/:username/:order").handler(ctx->{
            String username = ctx.pathParam("username");
            String order = ctx.pathParam("order");

            if(usersPort.containsKey(username)) {
                vertx.eventBus().request(EventBusTopics.ADD_ORDER, username + ";" + order, ar->{
                    if(ar.succeeded())
                        ctx.request().response().end(String.format((String) ar.result().body()));
                });
            }
            else
                ctx.request().response().end(String.format("you are not logged in"));
        });

        router.get("/GetOrders/:username").handler(ctx->{
            String username = ctx.pathParam("username");
            if(usersPort.containsKey(username)) {
                vertx.eventBus().request(EventBusTopics.GET_ORDERS,username, ar-> {
                    if (ar.succeeded())
                        ctx.request().response().end(String.format((String) ar.result().body()));
                });
            }
            else
                ctx.request().response().end(String.format("you are not logged in"));
        });


        vertx.createHttpServer().requestHandler(router).listen(start_port_num);

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        stopPromise.complete();
    }

    public Boolean Login(String username, String password) {
        Object obj = null;
        try {
            obj = this.parser.parse(new FileReader(this.usersDataPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = (JSONObject) obj;
        String pw = (String) jsonObject.get(username);

            return password.equals(pw);
    }

    public Boolean Logout(String username){
        if(usersPort.containsKey(username)) {
            usersPort.get(username).getValue().close();
            usersPort.remove(username);
            return true;
        }
        return  false;
    }
}
