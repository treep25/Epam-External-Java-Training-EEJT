package com.epam.http;

import com.epam.product.ProductRepo;
import com.epam.AbstractFactory;
import com.epam.Server;


public class HttpServerFactory implements AbstractFactory {

    @Override
    public Server getServer(int port, ProductRepo productRepo) {
        return new HttpServer(port, productRepo);
    }
}
