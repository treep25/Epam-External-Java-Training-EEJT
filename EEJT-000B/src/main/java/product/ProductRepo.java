package product;

import java.util.List;

public class ProductRepo {
    private List<Product> productList = List.of(
            new Product(1L, "Weed chair", 1200L),
            new Product(2L, "Table", 300L),
            new Product(3L, "Laptop", 3300L),
            new Product(4L, "Notebook", 2500L),
            new Product(5L, "T-shirt", 12L),
            new Product(6L, "NoteBook bag ", 300L),
            new Product(7L, "Camp chair", 235L),
            new Product(8L, "Door", 900L),
            new Product(9L, "Iphone", 3000L),
            new Product(10L, "Bluetooth micro", 12000L));

    public List<Product> getProducts() {
        return productList;
    }
}
