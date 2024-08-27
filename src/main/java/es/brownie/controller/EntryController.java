package es.brownie.controller;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class EntryController {

    Logger LOGGER = Logger.getLogger(EntryController.class.getName());

    private HttpServer server;

    public EntryController(HttpHandler consumer) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", consumer);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    public void start() {
        LOGGER.info("Starting server");
        server.start();
    }

    public void stop() {
        LOGGER.info("Stopping server");
        server.stop(0);
    }

}
