package es.brownie;

import es.brownie.controller.LoadBalancerEntryController;
import es.brownie.service.LoadBalancerService;

import java.io.IOException;

public class LoadBalancerApplication {
    public static void main(String[] args) throws IOException {

        var service = new LoadBalancerService();

        new LoadBalancerEntryController(service);

    }
}