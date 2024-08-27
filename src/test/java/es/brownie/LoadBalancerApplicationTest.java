package es.brownie;

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

class LoadBalancerApplicationTest {

    private final static Logger LOGGER = Logger.getLogger(LoadBalancerApplicationTest.class.getName());

    private DummyNode node1;
    private DummyNode node2;

    @BeforeEach
    void setUp() throws IOException {

        node1 = new DummyNode(9001);
        node2 = new DummyNode(9002);

        new Thread(node1::start).start();
        new Thread(node2::start).start();

    }

    @AfterEach
    void afterEach() {
        node1.stop();
        node2.stop();
    }

    @Test
    void simpleTest() throws IOException, URISyntaxException, InterruptedException {

        LoadBalancerApplication app = new LoadBalancerApplication();

        new Thread(app::start).start();

        sendHttpRequest();
        sendHttpRequest();

        assertEquals(1, node1.getCounter().get());
        assertEquals(1, node2.getCounter().get());

        app.stop();

    }

    @Test
    void harderTest() throws IOException, URISyntaxException, InterruptedException {

        LoadBalancerApplication app = new LoadBalancerApplication();

        new Thread(app::start).start();

        Thread.sleep(1000);

        IntStream.range(0, 1000).parallel().forEach(i -> sendHttpRequest());
        assertEquals(500, node1.getCounter().get());
        assertEquals(500, node2.getCounter().get());


        app.stop();

    }


    /* --- */

    private HttpClient client = HttpClient.newBuilder().build();

    private AtomicInteger atomicInteger = new AtomicInteger();

    private void sendHttpRequest() {

        try {


            HttpRequest request = null;

            int incrementAndGet = atomicInteger.incrementAndGet();

            LOGGER.info("Sending request " + incrementAndGet);

            request = HttpRequest.newBuilder(new URI("http://localhost:" + 8080)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}