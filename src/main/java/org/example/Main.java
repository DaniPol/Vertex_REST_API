package org.example;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.example.verticles.MainVerticle;
import org.example.verticles.OrderVerticle;
import org.example.verticles.RestVerticle;

public class Main {
    public static void main(String[] args) throws Exception {
        String username = "dani", password = "12312";

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(MainVerticle.class, new DeploymentOptions(), ar1->{
            if(ar1.succeeded())
                vertx.deployVerticle(RestVerticle.class, new DeploymentOptions(), ar2->{
                    if(ar2.succeeded())
                        vertx.deployVerticle(OrderVerticle.class, new DeploymentOptions());
                });
        });
    }
}