package http.commands;

import http.MapResponse;
import product.ProductRepo;
import services.Command;
import http.JsonResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetItemByNumberHTTPCommand implements Command<JsonResponse> {
    private final String id;
    private final ProductRepo productRepo;

    public GetItemByNumberHTTPCommand(String id, ProductRepo productRepo) {
        this.id = id;
        this.productRepo = productRepo;
    }

    private boolean isFit() {
        return !id.isBlank() && Integer.parseInt(id) - 1 >= 0 && Integer.parseInt(id) <= productRepo.getProducts().size();
    }

    @Override
    public JsonResponse execute() {
        if (isFit()) {
            return new MapResponse(new LinkedHashMap<>() {{
                put("name", productRepo.getProducts().get(Integer.parseInt(id) - 1).name());
                put("price", String.valueOf(productRepo.getProducts().get(Integer.parseInt(id) - 1).price()));
            }});
        }
        return new MapResponse(Map.of("Oops", "We have not such product"));
    }
}
