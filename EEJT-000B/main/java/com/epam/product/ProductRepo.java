package com.epam.product;

import java.util.Map;

public class ProductRepo {
    private Map<Long, Product> productMap = Map.of(
            1L, new Product("Weed chair", 1200L),
            2L, new Product("Table", 300L),
            3L, new Product("Laptop", 3300L),
            4L, new Product("Notebook", 2500L),
            5L, new Product("T-shirt", 12L),
            6L, new Product("NoteBook bag ", 300L),
            7L, new Product("Camp chair", 235L),
            8L, new Product("Door", 900L),
            9L, new Product("Iphone", 3000L),
            10L, new Product("Bluetooth micro", 12000L));

    public int getSize() {
        return productMap.size();
    }

    public Product getItem(Long id) {
        return productMap.get(id);
    }
}
