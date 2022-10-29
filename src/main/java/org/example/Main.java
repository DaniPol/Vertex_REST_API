package org.example;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.example.vetricles.MainVerticle;
import org.example.vetricles.OrderVerticle;
import org.example.vetricles.RestVerticle;

public class Main {
    public static void main(String[] args) {
        String id;
        String pw;

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class, new DeploymentOptions());
        vertx.deployVerticle(RestVerticle.class, new DeploymentOptions());
        vertx.deployVerticle(OrderVerticle.class, new DeploymentOptions());




    }
}