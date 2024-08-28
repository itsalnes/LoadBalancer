package es.brownie.strategies;

import es.brownie.model.ServerNode;

import java.util.Collection;
import java.util.Comparator;

public class SameTotalRequestsStrategy implements IBalancingStrategy {

    @Override
    public ServerNode chooseNode(Collection<ServerNode> nodes) {

        return nodes.stream().filter(ServerNode::isHealthy).min(
                Comparator.comparingInt(n -> n.getCounter().get())
        ).orElseThrow();
    }
}
