package es.brownie;

import es.brownie.controller.CommandController;
import es.brownie.controller.EntryController;
import es.brownie.service.BalancerService;
import es.brownie.strategies.IBalancingStrategy;
import es.brownie.strategies.SameTotalRequestsStrategy;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoadBalancerApplication {

    private final BalancerService balancerService = new BalancerService(new SameTotalRequestsStrategy());

    private final EntryController entryController = new EntryController(balancerService);

    private final CommandController commandController = new CommandController(balancerService);

    public LoadBalancerApplication() throws IOException, URISyntaxException {
    }

    void start() {
        entryController.start();
    }

    void stop() {
        entryController.stop();
        commandController.stop();
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