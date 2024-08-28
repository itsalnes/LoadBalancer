package es.brownie;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class AbstractLoadBalancerTest {

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public void registerNodeOnLoadBalancer(int port) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:9090/command/addNode"))
                    .POST(ofString("localhost:" + port))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deregisterNodeOnLoadBalancer(int port) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:9090/command/removeNode"))
                    .POST(ofString("localhost:" + port))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
