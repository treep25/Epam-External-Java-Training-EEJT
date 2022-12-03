package http;

import product.ProductRepo;
import services.AbstractFactory;
import services.Server;


public class HttpServerFactory implements AbstractFactory {

    @Override
    public Server getServer(int port, ProductRepo productRepo) {
        return new HttpServer(port, productRepo);
    }
}
