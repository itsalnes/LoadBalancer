package es.brownie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LoadBalancerApplicationTest {

    private DummyNode node1;
    private DummyNode node2;

    @BeforeEach
    void setUp() throws IOException {

        node1 = new DummyNode(8085);
        node2 = new DummyNode(8086);

        new Thread(node1::start);
        new Thread(node2::start);

    }

    @AfterEach
    void afterEach() {
        node1.stop();
        node2.stop();
    }

    @Test
    void simpleTest() {

    }

}