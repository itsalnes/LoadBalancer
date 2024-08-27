package es.brownie.service;

import com.sun.net.httpserver.HttpExchange;
import es.brownie.model.ServerNode;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class DelegateRequestService {

    private final static Logger LOGGER = Logger.getLogger(DelegateRequestService.class.getName());

    private final HttpClient client = HttpClient.newBuilder().build();

    public void handle(ServerNode node, HttpExchange exchange) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(node.getUri()).GET().build();

        LOGGER.info("Sending message to " + node);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LOGGER.info("Received response from " + node);

        exchange.sendResponseHeaders(response.statusCode(), response.body().length());
        exchange.getResponseBody().write(response.body().getBytes());
        exchange.close();

        LOGGER.info("Repplied to origin of request to " + node);

    }

}
