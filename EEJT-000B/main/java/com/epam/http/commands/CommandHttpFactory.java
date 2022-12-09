package com.epam.http.commands;

import com.epam.http.JsonResponse;
import com.epam.product.ProductRepo;
import com.epam.Command;


import java.util.Map;

public class CommandHttpFactory {
    private final ProductRepo productRepo;
    private final Map<String, Command<JsonResponse>> commandMap;

    public CommandHttpFactory(ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.commandMap = Map.of(
                "GET/shop/count", new CountAmountOfProductsHTTPCommand(productRepo),
                "GET/shop/item?get_info=", new GetItemByNumberHTTPCommand(productRepo)
        );
    }

    public Command<JsonResponse> getCommand(String request) {
        Command<JsonResponse> command;
        if (request.matches("GET/shop/item\\?get_info=\\d+")) {
            request = request.replaceAll("\\d+", "");
            command = commandMap.get(request);
        } else {
            command = commandMap.getOrDefault(request, new NoSuchCommand());
        }
        return command;
    }
}
