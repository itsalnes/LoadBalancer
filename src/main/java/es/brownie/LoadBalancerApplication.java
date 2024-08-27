package es.brownie;

import es.brownie.controller.EntryController;
import es.brownie.service.BalancerService;
import es.brownie.strategies.IBalancingStrategy;
import es.brownie.strategies.SameTotalRequestsStrategy;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoadBalancerApplication {

    private final BalancerService balancerService = new BalancerService(new SameTotalRequestsStrategy());

    private final EntryController entryController = new EntryController(balancerService);

    public LoadBalancerApplication() throws IOException, URISyntaxException {
    }

    void start() {
        entryController.start();
    }

    void stop() {
        entryController.stop();
    }

    /* --- */

    public void setBalancingStrategy(IBalancingStrategy strategy) {
        balancerService.setStrategy(strategy);
    }

    /* --- */

    public static void main(String[] args) throws IOException, URISyntaxException {
        var app = new LoadBalancerApplication();
        app.start();

    }

}