package services.factory;

import services.Server;

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
