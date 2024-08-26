package es.brownie;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/** Increments a counter and returns 200 */
public class DummyNode {

    private AtomicInteger counter = new AtomicInteger(0);

    private HttpServer server;

    public DummyNode(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        final HttpHandler handler = exchange -> {
            counter.incrementAndGet();
        };

        server.createContext("/", handler);
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(100);
    }

}
