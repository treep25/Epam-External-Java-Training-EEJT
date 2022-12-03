package tcp.commands;

import product.ProductRepo;
import services.Command;
import tcp.Response;
import tcp.StringResponse;

public class CountAmountOfProductsTCPCommand implements Command<StringResponse> {
    private final ProductRepo productRepo;

    public CountAmountOfProductsTCPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public StringResponse execute() {
        return new Response(String.valueOf(productRepo.getProducts().size()));
    }
}
