package services.factory;

import services.Server;

public class TcpServerFactory extends AbstractFactory {
    Server server;

    public TcpServerFactory(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.run();
    }
}
