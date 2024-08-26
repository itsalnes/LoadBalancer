package es.brownie.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.brownie.model.ServerNode;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class LoadBalancerService implements HttpHandler {

    private DelegateRequestService delegateRequestService = new DelegateRequestService();

    private List<ServerNode> nodes = List.of(new ServerNode("http://localhost:8085"), new ServerNode("http://localhost:8086"));

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        var choice = nodes.stream().min(
                Comparator.comparingInt(n -> n.getCounter().get())
        ).orElseThrow();

        choice.getCounter().incrementAndGet();

        delegateRequestService.handle(choice, exchange);

    }



}
