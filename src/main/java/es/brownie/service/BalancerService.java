package es.brownie.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.brownie.interfaces.INodeManager;
import es.brownie.model.ServerNode;
import es.brownie.strategies.IBalancingStrategy;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class BalancerService implements HttpHandler, INodeManager {

    private final static Logger LOGGER = Logger.getLogger(BalancerService.class.getName());

    private final DelegateRequestService delegateRequestService = new DelegateRequestService();

    private final List<ServerNode> nodes = new CopyOnWriteArrayList<>();
    private IBalancingStrategy strategy;

    public BalancerService(IBalancingStrategy strategy) {
        this.strategy = strategy;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        var choice = strategy.chooseNode(nodes);

        LOGGER.info("Chosen node: " + choice.getUri().toString());

        choice.getCounter().incrementAndGet();

        try {
            delegateRequestService.handle(choice, exchange);

            //Notify the strategy that the node responded, in case it wants to modify the statistical data
            strategy.afterResponse(choice);

            LOGGER.info("Message to " + choice.getUri().toString() + " handled!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /* --- */

    public void addNode(String uri) {
        nodes.add(new ServerNode("http://" + uri));
    }

    public void removeNode(String uri) {
        var nodeOpt = nodes.stream().filter(node -> node.getUri().toString().endsWith(uri)).findAny();
        nodeOpt.ifPresent(nodes::remove);
    }

    /* --- */

    public void setStrategy(IBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    /* --- FOR TESTING --- */

    List<ServerNode> getNodes() {
        return nodes;
    }
}
