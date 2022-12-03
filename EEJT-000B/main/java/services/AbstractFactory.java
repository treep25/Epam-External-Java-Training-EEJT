package services;

import product.ProductRepo;

public interface AbstractFactory {
    Server getServer(int port, ProductRepo productRepo);
}
