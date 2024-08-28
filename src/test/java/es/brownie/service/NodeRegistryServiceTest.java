package es.brownie.service;

import es.brownie.strategies.SameConcurrentLoadStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeRegistryServiceTest {

    private NodeRegistryService service;

    @BeforeEach
    void setup() {
        service = new NodeRegistryService(new SameConcurrentLoadStrategy());
    }

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

    @Test
    void max10Nodes() {
        for (int i = 9001; i <= 9015; i++) {
            service.addNode("localhost:" + i);
        }

        assertEquals(10, service.getNodes().size());
    }

    @Test
    void max10NodesConcurrent() {

        IntStream.range(9001, 19999).parallel().forEach(
                i -> service.addNode("localhost:" + i)
        );

        assertEquals(10, service.getNodes().size());
    }

    @Test
    void noDuplicates() {
        service.addNode("localhost:1234");
        service.addNode("localhost:1234");

        assertEquals(1, service.getNodes().size());
    }

    @Test
    void noDuplicatesConcurrent() {

        IntStream.range(1, 1723).parallel().forEach(
                i -> {
                    if (i % 4 == 0)
                        service.removeNode("localhost:2000");
                    else
                        service.addNode("localhost:2000");
                }
        );

        assertTrue(service.getNodes().size() <= 1);
    }

}