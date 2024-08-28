package es.brownie;

import es.brownie.controller.CommandController;
import es.brownie.controller.EntryController;
import es.brownie.service.BalancerService;
import es.brownie.service.NodeRegistryService;
import es.brownie.strategies.IBalancingStrategy;
import es.brownie.strategies.SameTotalRequestsStrategy;

import java.io.IOException;

public class ApplicationContext {

    private final NodeRegistryService nodeRegistryService = new NodeRegistryService(new SameTotalRequestsStrategy());

    private final BalancerService balancerService = new BalancerService(nodeRegistryService);

    private final EntryController entryController = new EntryController(balancerService);

    private final CommandController commandController = new CommandController(nodeRegistryService);

    public ApplicationContext() throws IOException {
    }

    void start() {
        entryController.start();
    }

    void stop() {
        entryController.stop();
        commandController.stop();
    }

    /* --- TEST ONLY --- */

    public void setBalancingStrategy(IBalancingStrategy strategy) {
        nodeRegistryService.setStrategy(strategy);
    }

}
