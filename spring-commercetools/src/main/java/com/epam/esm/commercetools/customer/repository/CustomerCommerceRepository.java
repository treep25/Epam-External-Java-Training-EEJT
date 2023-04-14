package com.epam.esm.commercetools.customer.repository;

import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.customer.model.CommerceCustomer;

import java.util.List;

public interface CustomerCommerceRepository<T> {
    T create(CommerceCustomer customer);

    List<T> read(PagePaginationBuilder pageRequest);

    T readById(String id);

    void delete(String id, long version);

    long getProductVersion(String id);
}
