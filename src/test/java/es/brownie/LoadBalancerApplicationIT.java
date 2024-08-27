package es.brownie;

import es.brownie.strategies.SameConcurrentLoadStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadBalancerApplicationIT extends AbstractLoadBalancerTest {

    private final static Logger LOGGER = Logger.getLogger(LoadBalancerApplicationIT.class.getName());

    private DummyNode node1;
    private DummyNode node2;

    private DummyNode node3;


    private LoadBalancerApplication app;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException, InterruptedException {

        // We simulate nodes of different capacity
        node1 = new DummyNode(9001, 50);
        node2 = new DummyNode(9002, 100);
        node3 = new DummyNode(9003, 150);

        new Thread(node1::start).start();
        new Thread(node2::start).start();
        new Thread(node3::start).start();

        app = new LoadBalancerApplication();

        new Thread(app::start).start();
        Thread.sleep(250);

        registerNodeOnLoadBalancer(9001);
        registerNodeOnLoadBalancer(9002);
        registerNodeOnLoadBalancer(9003);

    }

    @AfterEach
    void afterEach() {
        app.stop();
        node1.stop();
        node2.stop();
        node3.stop();
    }

    @Test
    void simpleTest() {

        sendHttpRequest();
        sendHttpRequest();
        sendHttpRequest();

        assertEquals(1, node1.getCounter().get());
        assertEquals(1, node2.getCounter().get());
        assertEquals(1, node3.getCounter().get());

    }

    @Test
    void harderTest() {

        IntStream.range(0, 99).parallel().forEach(i -> sendHttpRequest());

        LOGGER.info("Node 1 received " + node1.getCounter().get() + " requests");
        LOGGER.info("Node 2 received " + node2.getCounter().get() + " requests");
        LOGGER.info("Node 3 received " + node3.getCounter().get() + " requests");

        assertEquals(33, node1.getCounter().get());
        assertEquals(33, node2.getCounter().get());
        assertEquals(33, node3.getCounter().get());

        app.stop();

    }

    @Test
    void sameConcurrentLoadStrategy() throws IOException, URISyntaxException, InterruptedException {

        LoadBalancerApplication app = new LoadBalancerApplication();
        app.setBalancingStrategy(new SameConcurrentLoadStrategy());

        new Thread(app::start).start();

        Thread.sleep(1000);

        IntStream.range(0, 200).parallel().forEach(i -> sendHttpRequest());

        LOGGER.info("Node 1 received " + node1.getCounter().get() + " requests");
        LOGGER.info("Node 2 received " + node2.getCounter().get() + " requests");
        LOGGER.info("Node 3 received " + node3.getCounter().get() + " requests");

        assertTrue(node1.getCounter().get() >= node2.getCounter().get());
        assertTrue(node2.getCounter().get() >= node3.getCounter().get());

        app.stop();

    }

    /* --- */

    private HttpClient client = HttpClient.newBuilder().build();

    private AtomicInteger atomicInteger = new AtomicInteger();


    private void sendHttpRequest() {

        try {

            int incrementAndGet = atomicInteger.incrementAndGet();

            LOGGER.info("Sending request #" + incrementAndGet);

            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:" + 8080)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}