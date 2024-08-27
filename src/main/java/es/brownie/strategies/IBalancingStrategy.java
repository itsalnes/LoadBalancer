package es.brownie.strategies;

import es.brownie.model.ServerNode;

import java.util.List;

public interface IBalancingStrategy {

    /**
     * Choose the node to use among the peers
     */
    ServerNode chooseNode(List<ServerNode> nodes);

    /**
     * Actions to execute after a node response, default: do nothing
     */
    default void afterResponse(ServerNode node) {
    }

}
