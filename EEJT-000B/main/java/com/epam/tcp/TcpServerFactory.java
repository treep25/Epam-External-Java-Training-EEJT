package com.epam.tcp;

import com.epam.product.ProductRepo;
import com.epam.AbstractFactory;
import com.epam.Server;

public class TcpServerFactory implements AbstractFactory {

    @Override
    public Server getServer(int port, ProductRepo productRepo) {
        return new TcpServer(port, productRepo);
    }
}
