package es.brownie.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.brownie.model.ServerNode;
import es.brownie.strategies.IBalancingStrategy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class BalancerService implements HttpHandler {

    private final static Logger LOGGER = Logger.getLogger(BalancerService.class.getName());

    private final DelegateRequestService delegateRequestService = new DelegateRequestService();

    private final List<ServerNode> nodes = List.of(
            new ServerNode("http://localhost:9001"),
            new ServerNode("http://localhost:9002"),
            new ServerNode("http://localhost:9003")
    );

    private IBalancingStrategy strategy;

    public BalancerService(IBalancingStrategy strategy) throws URISyntaxException {
        this.strategy = strategy;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        var choice = strategy.chooseNode(nodes);

        LOGGER.info("Chosen node: " + choice.getUri().toString());

        choice.getCounter().incrementAndGet();

        try {
            delegateRequestService.handle(choice, exchange);

            //Notify the strategy that the node responded, in case it wants to modify the data
            strategy.afterResponse(choice);

            LOGGER.info("Message to " + choice.getUri().toString() + " handled!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /* --- */

    public void setStrategy(IBalancingStrategy strategy) {
        this.strategy = strategy;
    }

}
