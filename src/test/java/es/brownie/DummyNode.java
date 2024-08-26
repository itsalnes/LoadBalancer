package es.brownie;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.brownie.common.MyHttpServer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyNode extends MyHttpServer implements HttpHandler {

    private AtomicInteger counter = new AtomicInteger(0);

    private HttpHandler handler ;

    public DummyNode(int port) throws IOException {
        super(port, null);
    }


}
