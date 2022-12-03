package http.commands;

import http.JsonResponse;
import http.MapResponse;
import product.ProductRepo;
import services.Command;

import java.util.Map;

public class CountAmountOfProductsHTTPCommand implements Command<JsonResponse> {
    private final ProductRepo productRepo;

    public CountAmountOfProductsHTTPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public JsonResponse execute() {
        return new MapResponse(Map.of("count", String.valueOf(productRepo.getProducts().size())));
    }
}
