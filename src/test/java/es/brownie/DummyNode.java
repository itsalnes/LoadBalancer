package es.brownie;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Increments a counter and returns 200, takes a random time to reply
 */
public class DummyNode {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final HttpServer server;

    private final Random random = new Random();

    public DummyNode(int port, long averageResponseTime) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        final HttpHandler handler = exchange -> {

            try {
                Thread.sleep(random.nextLong(averageResponseTime * 2)); //Simulate a variable response time
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
            counter.incrementAndGet();
        };

        server.createContext("/", handler);
        server.setExecutor(newFixedThreadPool(4));
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
