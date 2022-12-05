package com.epam.tcp.commands;

import com.epam.product.Product;
import com.epam.product.ProductRepo;
import com.epam.Command;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;

public class GetItemByNumberTCPCommand implements Command<StringResponse> {
    ProductRepo productRepo;

    public GetItemByNumberTCPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;

    }

    private boolean isFit(String request) {
        return !request.isBlank() && Long.parseLong(request) > 0 && Integer.parseInt(request) <= productRepo.getSize();
    }

    @Override
    public StringResponse execute(String request) {
        request = request.replaceAll("\\D+", "");
        if (isFit(request)) {
            Product product = productRepo.getItem(Long.parseLong(request));
            return new Response(product.name() + "|" + product.price());
        }
        return new Response("We have not got such item in our shop");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetItemByNumberTCPCommand)) return false;

        GetItemByNumberTCPCommand that = (GetItemByNumberTCPCommand) o;

        return productRepo.equals(that.productRepo);
    }

    @Override
    public int hashCode() {
        return productRepo.hashCode();
    }
}
