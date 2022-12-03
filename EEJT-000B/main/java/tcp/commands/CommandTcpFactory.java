package tcp.commands;

import product.ProductRepo;
import services.Command;
import tcp.StringResponse;

import java.util.Map;

public class CommandTcpFactory {
    private final ProductRepo productRepo;

    public CommandTcpFactory(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Map<String, Command<StringResponse>> getCommandsMap(String request) {
        String parsedRequest = request.replaceAll("\\D", "");
        return Map.of("<get count>", new CountAmountOfProductsTCPCommand(productRepo),
                "<get item = " + parsedRequest + ">", new GetItemByNumberTCPCommand(parsedRequest, productRepo));
    }

}
