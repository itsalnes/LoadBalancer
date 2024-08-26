package es.brownie.common;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class MyHttpServer {

    protected abstract HttpHandler getHandler();

    public MyHttpServer(int port, HttpHandler handler) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", handler);
        server.setExecutor(null);
        server.start();
    }
}
