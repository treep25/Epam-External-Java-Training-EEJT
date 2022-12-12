package com.epam.http.commands;

import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.product.Product;
import com.epam.product.ProductRepo;
import com.epam.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetItemByNumberHTTPCommand implements Command<JsonResponse> {
    private final ProductRepo productRepo;
    private final MapResponse formatInput = new MapResponse(Map.of("Oops", "We have not such product"));

    public GetItemByNumberHTTPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    private boolean isFit(String id) {
        return !id.isBlank() && Long.parseLong(id) > 0 && Long.parseLong(id) <= productRepo.getSize();
    }

    @Override
    public JsonResponse execute(String request) {
        request = request.replaceAll("\\D+", "");
        if (isFit(request)) {
            Product product = productRepo.getItem(Long.parseLong(request));
            return new MapResponse(new LinkedHashMap<>() {{
                put("name", product.name());
                put("price", String.valueOf(product.price()));
            }});
        }
        return formatInput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetItemByNumberHTTPCommand that)) return false;

        if (!productRepo.equals(that.productRepo)) return false;
        return formatInput.equals(that.formatInput);
    }

    @Override
    public int hashCode() {
        int result = productRepo.hashCode();
        result = 31 * result + formatInput.hashCode();
        return result;
    }
}
