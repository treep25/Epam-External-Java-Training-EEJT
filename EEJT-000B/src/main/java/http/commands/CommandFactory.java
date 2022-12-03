package http.commands;

import product.ProductRepo;
import services.Command;
import http.JsonResponse;

import java.util.Map;

public class CommandFactory {
    private final ProductRepo productRepo;

    public CommandFactory(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Map<String, Command<JsonResponse>> getCommandsMap(String request) {
        String parsedRequest = request.replaceAll("\\D", "");
        return Map.of("GET/shop/count", new CountAmountOfProductsHTTPCommand(productRepo),
                "GET/shop/item?get_info=" + parsedRequest, new GetItemByNumberHTTPCommand(parsedRequest, productRepo));
    }
}
