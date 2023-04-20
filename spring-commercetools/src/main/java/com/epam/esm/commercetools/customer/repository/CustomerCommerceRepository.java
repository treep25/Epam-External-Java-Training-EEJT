package com.epam.esm.commercetools.customer.repository;

import com.commercetools.api.models.customer.Customer;
import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.customer.model.CommerceCustomer;

import java.util.List;

public interface CustomerCommerceRepository {
    Customer create(CommerceCustomer customer);

    List<Customer> read(PagePaginationBuilder pageRequest);

    Customer readById(String id);

    void delete(String id, long version);

    long getProductVersion(String id);
}
