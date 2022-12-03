package tcp.commands;

import product.Product;
import product.ProductRepo;
import services.Command;

public class GetItemByNumberTCPCommand implements Command<String> {
    private final String request;

    public GetItemByNumberTCPCommand(String request) {
        this.request = request;
    }

    @Override
    public String execute() {
        ProductRepo productRepo = new ProductRepo();
        Product product = productRepo.getProducts().get(Integer.parseInt(request));
        return product.name() + "|" + product.price();
    }
}
