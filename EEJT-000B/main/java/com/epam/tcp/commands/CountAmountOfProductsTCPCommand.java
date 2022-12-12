package com.epam.tcp.commands;

import com.epam.product.ProductRepo;
import com.epam.Command;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;

public class CountAmountOfProductsTCPCommand implements Command<StringResponse> {
    private final ProductRepo productRepo;

    public CountAmountOfProductsTCPCommand(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public StringResponse execute(String request) {
        return new Response(String.valueOf(productRepo.getSize()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountAmountOfProductsTCPCommand)) return false;

        CountAmountOfProductsTCPCommand that = (CountAmountOfProductsTCPCommand) o;

        return productRepo.equals(that.productRepo);
    }

    @Override
    public int hashCode() {
        return productRepo.hashCode();
    }
}
