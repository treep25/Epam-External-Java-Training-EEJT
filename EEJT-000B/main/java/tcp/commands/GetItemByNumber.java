package tcp.commands;

import entity.Product;
import entity.ProductRepo;

public class GetItemByNumber implements Command<String> {
    private final String request;

    public GetItemByNumber(String request) {
        this.request = request;
    }

    @Override
    public String execute() {
        ProductRepo productRepo = new ProductRepo();
        Product product = productRepo.getSomeProducts().get(Integer.parseInt(request));
        return product.name() + "|" + product.price();
    }
}
