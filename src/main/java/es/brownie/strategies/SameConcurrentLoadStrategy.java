package es.brownie.strategies;

import es.brownie.model.ServerNode;

import java.util.Comparator;
import java.util.List;

public class SameConcurrentLoadStrategy implements IBalancingStrategy {
    @Override
    public ServerNode chooseNode(List<ServerNode> nodes) {
        return nodes.stream()
                .filter(ServerNode::isHealthy)
                .min(
                Comparator.comparingInt(n -> n.getCounter().get())
        ).orElseThrow();
    }

    @Override
    public void afterResponse(ServerNode node) {
        node.getCounter().decrementAndGet();
    }
}
