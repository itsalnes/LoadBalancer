package es.brownie.model;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerNode {

    private final String url;

    private final AtomicInteger counter = new AtomicInteger(0);

    public ServerNode(String url) {
        this.url = url;
    }

    /* --- */

    public String getUrl() {
        return url;
    }

    public AtomicInteger getCounter() {
        return counter;
    }
}
