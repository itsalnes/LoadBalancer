package es.brownie;

import es.brownie.controller.EntryController;
import es.brownie.service.BalancerService;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoadBalancerApplication {

    private final BalancerService balancerService = new BalancerService();

    private final EntryController entryController =  new EntryController(balancerService);

    public LoadBalancerApplication() throws IOException, URISyntaxException {
    }

    void start() {
        entryController.start();
    }

    void stop() {
        entryController.stop();
    }

    /* --- */

    public static void main(String[] args) throws IOException, URISyntaxException {
        var app = new LoadBalancerApplication();
        app.start();

    }

}