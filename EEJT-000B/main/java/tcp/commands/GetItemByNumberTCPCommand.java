package tcp.commands;

import product.ProductRepo;
import services.Command;
import tcp.Response;
import tcp.StringResponse;

public class GetItemByNumberTCPCommand implements Command<StringResponse> {
    private final String id;
    ProductRepo productRepo;

    public GetItemByNumberTCPCommand(String id, ProductRepo productRepo) {
        this.id = id;
        this.productRepo = productRepo;

    }

    private boolean isFit() {
        return !id.isBlank() && Integer.parseInt(id) - 1 >= 0 && Integer.parseInt(id) - 1 <= productRepo.getProducts().size();
    }

    @Override
    public StringResponse execute() {
        if (isFit()) {
            return new Response(productRepo.getProducts().get(Integer.parseInt(id)).name() + "|" + productRepo.getProducts().get(Integer.parseInt(id)).price());
        }
        return new Response("We have not got such item in our shop");
    }
}
