package tcp.commands;

import entity.ProductRepo;

public class CountAmountOfProductsCommand implements Command<String> {
    @Override
    public String execute() {
        ProductRepo productRepo = new ProductRepo();
        return "Amount of products is - " + productRepo.getSomeProducts().size();
    }
}
