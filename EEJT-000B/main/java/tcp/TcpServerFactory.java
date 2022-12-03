package tcp;

import product.ProductRepo;
import services.AbstractFactory;
import services.Server;

public class TcpServerFactory implements AbstractFactory {

    @Override
    public Server getServer(int port, ProductRepo productRepo) {
        return new TcpServer(port, productRepo);
    }
}
