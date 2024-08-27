package es.brownie.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerNode {

    private final URI uri;

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
