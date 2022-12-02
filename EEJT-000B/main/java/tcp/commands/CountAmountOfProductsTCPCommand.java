package tcp.commands;

import entity.ProductRepo;
import services.Command;

public class CountAmountOfProductsTCPCommand implements Command<String> {
    @Override
    public String execute() {
        ProductRepo productRepo = new ProductRepo();
        return String.valueOf(productRepo.getSomeProducts().size());
    }
}
