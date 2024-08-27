package es.brownie.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerNode {

    private final URI uri;

    private final AtomicBoolean healthy = new AtomicBoolean(true);

    /**
     * A counter for balancing strategies to use if needed, it can represent either total requests or concurrent requests
     * and this might not be the right place to store it
     */
    private final AtomicInteger counter = new AtomicInteger(0);

    public ServerNode(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /* --- */

    public URI getUri() {
        return uri;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "ServerNode{" +
                "uri=" + uri +
                ", counter=" + counter +
                '}';
    }

    public boolean isHealthy() {
        return healthy.get();
    }

    public void setHealthy(boolean value) {
        healthy.set(value);
    }

}
