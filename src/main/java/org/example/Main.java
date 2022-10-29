package org.example;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.example.vetricles.MainVerticle;
import org.example.vetricles.OrderVerticle;
import org.example.vetricles.RestVerticle;

public class Main {
    public static void main(String[] args) {
        String id;
        String pw;

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