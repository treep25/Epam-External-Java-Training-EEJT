package com.epam.tcp.commands;

import com.epam.http.JsonResponse;
import com.epam.http.commands.CountAmountOfProductsHTTPCommand;
import com.epam.http.commands.GetItemByNumberHTTPCommand;
import com.epam.product.ProductRepo;
import com.epam.Command;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;

import java.util.Map;

public class CommandTcpFactory {
    private final ProductRepo productRepo;
    private final Map<String, Command<StringResponse>> commandMap;

    public CommandTcpFactory(ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.commandMap = Map.of(
                "<get count>", new CountAmountOfProductsTCPCommand(productRepo),
                "<get item = >", new GetItemByNumberTCPCommand(productRepo)
        );
    }

    public Command<StringResponse> getCommand(String request) {
        Command<StringResponse> command;
        if (request.matches("<get item = \\d+>")) {
            request = request.replaceAll("\\d+", "");
            command = commandMap.get(request);
        } else {
            command = commandMap.getOrDefault(request, new NoSuchCommand());
        }
        return command;
    }

}
