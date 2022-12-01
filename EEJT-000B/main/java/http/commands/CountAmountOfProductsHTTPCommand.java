package http.commands;

import entity.ProductRepo;
import services.Command;

import java.util.Map;

public class CountAmountOfProductsHTTPCommand implements Command<Map<String, String>> {
    @Override
    public Map<String, String> execute() {
        ProductRepo productRepo = new ProductRepo();
        return Map.of("count", String.valueOf(productRepo.getSomeProducts().size()));
    }
}
