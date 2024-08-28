package es.brownie.service;

import es.brownie.interfaces.INodeManager;
import es.brownie.model.ServerNode;
import es.brownie.strategies.IBalancingStrategy;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class NodeRegistryService implements INodeManager {

    private final ConcurrentHashMap<String, ServerNode> nodes = new ConcurrentHashMap<>();

    private IBalancingStrategy strategy;

    public NodeRegistryService(IBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public void addNode(String uri) {

        if (!nodes.containsKey(uri) && nodes.size() < 10) {
            synchronized (nodes) {
                if (nodes.size() < 10) {
                    nodes.put(uri, new ServerNode(uri));
                }
            }
        }
    }

    public void removeNode(String uri) {
        nodes.remove(uri);
    }

    /* --- */

    public ServerNode chooseNode() {
        return strategy.chooseNode(nodes.values());
    }

    public void afterResponse(ServerNode choice) {
        strategy.afterResponse(choice);
    }

    /* --- */

    public void setStrategy(IBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    Collection<ServerNode> getNodes() {
        return nodes.values();
    }
}
