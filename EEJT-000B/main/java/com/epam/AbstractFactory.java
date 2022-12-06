package com.epam;

import com.epam.product.ProductRepo;

public interface AbstractFactory {
    Server getServer(int port, ProductRepo productRepo);
}
