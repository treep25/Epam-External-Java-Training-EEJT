package com.epam;

import com.epam.http.HttpServerFactory;
import com.epam.product.ProductRepo;
import com.epam.AbstractFactory;
import com.epam.Server;
import com.epam.tcp.TcpServerFactory;

public class Starter {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();

        AbstractFactory tcp = new TcpServerFactory();
        AbstractFactory http = new HttpServerFactory();

        Server tcpServer = tcp.getServer(3009, productRepo);
        Server httpServer = http.getServer(8080, productRepo);

        tcpServer.start();
        httpServer.start();
    }
}
