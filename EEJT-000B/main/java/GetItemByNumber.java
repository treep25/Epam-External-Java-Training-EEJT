import entity.Product;

import java.util.List;

public class GetItemByNumber implements Command {
    private List<Product> productsList;
    private String response;
    private String request;

    public GetItemByNumber(List<Product> productsList, String request) {
        this.productsList = productsList;
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void execute() {
        Product product = productsList.get(Integer.parseInt(request));
        response = product.getName() + "|" + product.getPrice();
    }
}
