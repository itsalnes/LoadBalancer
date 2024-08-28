package es.brownie.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.brownie.exceptions.ConnectionFailedException;
import es.brownie.interfaces.INodeManager;

import java.io.IOException;
import java.util.logging.Logger;

public class BalancerService implements HttpHandler {

    private final static Logger LOGGER = Logger.getLogger(BalancerService.class.getName());

    private final DelegateRequestService delegateRequestService = new DelegateRequestService();

    private final NodeRegistryService nodeRegistryService;

    public BalancerService(NodeRegistryService nodeRegistryService) {

        this.nodeRegistryService = nodeRegistryService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        var choice = nodeRegistryService.chooseNode();

        LOGGER.info("Chosen node: " + choice.getUri().toString());

        choice.getCounter().incrementAndGet();

        try {
            delegateRequestService.handle(choice, exchange);

            //Notify the strategy that the node responded, in case it wants to modify the statistical data
            nodeRegistryService.afterResponse(choice);

            LOGGER.info("Message to " + choice.getUri().toString() + " handled!");
        } catch (ConnectionFailedException e) {
            LOGGER.warning("Connection to " + choice + " failed, retrying somewhere else");
            choice.setHealthy(false);
            handle(exchange);
        }

    }

}
