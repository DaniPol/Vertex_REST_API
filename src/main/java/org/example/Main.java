package org.example;


import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.example.verticles.OrderVerticle;
import org.example.verticles.RestVerticle;

public class Main {
    public static void main(String[] args) throws Exception {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(OrderVerticle.class, new DeploymentOptions(), ar1 -> {
            if (ar1.succeeded())
                vertx.deployVerticle(RestVerticle.class, new DeploymentOptions());
        });


//        ClusterManager mgr = new HazelcastClusterManager();
//        VertxOptions options = new VertxOptions().setClusterManager(mgr);
//        Vertx.clusteredVertx(options,res->{
//            if(res.succeeded()){
//                System.out.println("Cluster created");
//                Vertx vertx = res.result();
//                vertx.deployVerticle(new OrderVerticle(),res1->{
//                    if (res1.succeeded()) {
//                        System.out.println("deployVerticle deployed");
//                        vertx.deployVerticle(new RestVerticle(), res2 -> {
//                            if (res2.succeeded())
//                                System.out.println("RestVerticle deployed");
//                            else
//                                System.out.println("RestVerticle not deployed");
//                        });
//                    }
//                    else
//                        System.out.println("deployVerticle not deployed");
//                });
//            }
//            else
//                System.out.println("Cluster not created");
//
//        });

    }
}
