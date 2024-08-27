package es.brownie.service;

import es.brownie.strategies.SameConcurrentLoadStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalancerServiceTest {

    private BalancerService service = new BalancerService(new SameConcurrentLoadStrategy());

    @Test
    void addNode() {
        service.addNode("localhost:9001");
        assertTrue(service.getNodes().stream().anyMatch(node -> node.getUri().toString().endsWith("localhost:9001")));
    }

    @Test
    void removeNode() {
        service.addNode("localhost:9001");
        service.removeNode("localhost:9001");
        assertTrue(service.getNodes().stream().noneMatch(node -> node.getUri().toString().endsWith("localhost:9001")));
    }

}