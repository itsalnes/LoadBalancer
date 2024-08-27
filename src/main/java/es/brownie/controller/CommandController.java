package es.brownie.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import es.brownie.interfaces.INodeManager;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class CommandController {

    private final static Logger LOGGER = Logger.getLogger(CommandController.class.getName());

    private final INodeManager nodeManager;

    private HttpServer server;

    public CommandController(INodeManager nodeManager) throws IOException {
        this.nodeManager = nodeManager;

        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/command", new CommandRequestHandler());
        server.setExecutor(null); // Only one command at the time
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private class CommandRequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            var requestPath = exchange.getRequestURI().getPath();

            var command = requestPath.substring(requestPath.lastIndexOf("/") + 1);

            switch (command) {
                case "addNode":
                    nodeManager.addNode(IOUtils.toString(exchange.getRequestBody(), Charset.defaultCharset()));
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    break;
                case "removeNode":
                    nodeManager.removeNode(IOUtils.toString(exchange.getRequestBody(), Charset.defaultCharset()));
                    exchange.sendResponseHeaders(204, 0);
                    exchange.close();
                    break;
                default:
                    exchange.sendResponseHeaders(500, 0);
                    exchange.close();
                    break;
            }

        }
    }

}
