package com.epam.http.commands;

import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.product.ProductRepo;
import com.epam.Command;

import java.util.Map;

public class CountAmountOfProductsHTTPCommand implements Command<JsonResponse> {
    private final ProductRepo productRepo;

    public CountAmountOfProductsHTTPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public JsonResponse execute(String request) {
        return new MapResponse(Map.of("count", String.valueOf(productRepo.getSize())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountAmountOfProductsHTTPCommand)) return false;

        CountAmountOfProductsHTTPCommand that = (CountAmountOfProductsHTTPCommand) o;

        return productRepo.equals(that.productRepo);
    }

    @Override
    public int hashCode() {
        return productRepo.hashCode();
    }
}
