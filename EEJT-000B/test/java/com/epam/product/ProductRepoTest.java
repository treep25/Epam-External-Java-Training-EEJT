package com.epam.product;

import com.epam.product.Product;
import com.epam.product.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepoTest {
    private Map<Long, Product> productMapTest = Map.of(
            1L, new Product("qwerty1", 1L),
            2L, new Product("qwerty2", 2L),
            3L, new Product("qwerty3", 3L),
            4L, new Product("qwerty4", 4L)
    );

    @Test
    void getProductsByIdTest() {
        assertEquals(new Product("qwerty1", 1L), productMapTest.get(1L));
        assertEquals(new Product("qwerty2", 2L), productMapTest.get(2L));
        assertEquals(new Product("qwerty3", 3L), productMapTest.get(3L));

    }

    @Test
    void IndexOutOfBoundsTest() {
        assertNull(productMapTest.get(12345678L));
    }
}