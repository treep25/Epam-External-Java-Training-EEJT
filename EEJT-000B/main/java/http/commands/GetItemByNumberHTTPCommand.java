package http.commands;

import entity.ProductRepo;
import services.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetItemByNumberHTTPCommand implements Command<Map<String, String>> {
    private final String id;
    private final ProductRepo productRepo = new ProductRepo();

    public GetItemByNumberHTTPCommand(String id) {
        this.id = id;
    }

    private boolean isFit() {
        return id != null && Integer.parseInt(id) - 1 > 0 && Integer.parseInt(id) <= productRepo.getSomeProducts().size();
    }

    @Override
    public Map<String, String> execute() {
        if (isFit()) {
            return new LinkedHashMap<>() {{
                put("name", productRepo.getSomeProducts().get(Integer.parseInt(id) - 1).name());
                put("price", String.valueOf(productRepo.getSomeProducts().get(Integer.parseInt(id) - 1).price()));
            }};
        }
        return Map.of("ERROR", "Something went wrong");
    }
}
