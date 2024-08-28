package es.brownie;

import java.io.IOException;

public class LoadBalancerApplication {

    private ApplicationContext registry = new ApplicationContext();

    public LoadBalancerApplication() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        var app = new LoadBalancerApplication();
        app.registry.start();
    }

}