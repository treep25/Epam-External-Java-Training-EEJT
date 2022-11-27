import entity.Product;

import java.util.List;

public class CountAmountOfProductsCommand implements Command {

    private final List<Product> products;
    String countProducts = "0";

    public CountAmountOfProductsCommand(List<Product> products) {
        this.products = products;
    }

    public String getCountProducts() {
        return countProducts;
    }

    @Override
    public void execute() {
        countProducts = String.valueOf(products.size());
    }
}
