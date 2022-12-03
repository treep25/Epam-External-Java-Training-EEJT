package http;

import services.Server;
import services.AbstractFactory;

public class HttpServerFactory extends AbstractFactory {
    Server server;

    public HttpServerFactory(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.run();
    }
}
