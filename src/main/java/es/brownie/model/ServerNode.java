package es.brownie.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerNode {

    private final URI uri;

    /**
     * A counter for balancing strategies to use if needed, it can represent either total requests or concurrent requests
     * and this might not be the right place to store it
     */
    private final AtomicInteger counter = new AtomicInteger(0);

    public ServerNode(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
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
}
