package es.brownie.controller;

import es.brownie.AbstractLoadBalancerTest;
import es.brownie.interfaces.INodeManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

class CommandControllerTest extends AbstractLoadBalancerTest {

    private INodeManager nodeManager = mock(INodeManager.class);

    private CommandController controller;

    @BeforeEach
    void setUp() throws IOException {
        controller = new CommandController(nodeManager);
    }

    @AfterEach
    void ciao() {
        controller.stop();
    }

    @Test
    void addNode() throws URISyntaxException, IOException, InterruptedException {
        registerNodeOnLoadBalancer(9001);
        verify(nodeManager, times(1)).addNode("localhost:9001");
    }

    @Test
    void removeNode() throws URISyntaxException, IOException, InterruptedException {
        deregisterNodeOnLoadBalancer(9001);
        verify(nodeManager, times(1)).removeNode("localhost:9001");
    }

}