package es.brownie.controller;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LoadBalancerEntryController {

    public LoadBalancerEntryController(HttpHandler consumer) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        server.createContext("/", consumer);
        server.setExecutor(null);
        server.start();
    }

}
