package es.brownie;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/** Increments a counter and returns 200, takes a random time to reply */
public class DummyNode {

    private final AtomicInteger counter = new AtomicInteger(0);

    private HttpServer server;

    private Random random = new Random();

    public DummyNode(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        final HttpHandler handler = exchange -> {
            counter.incrementAndGet();

            try {
                Thread.sleep(random.nextLong(150)); //Simulate a variable response time
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        };

        server.createContext("/", handler);
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    /* --- */

    public AtomicInteger getCounter() {
        return counter;
    }

}
