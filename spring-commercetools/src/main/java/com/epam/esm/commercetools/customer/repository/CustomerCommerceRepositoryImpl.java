package com.epam.esm.commercetools.customer.repository;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerDraft;
import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.customer.model.CommerceCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerCommerceRepositoryImpl implements CustomerCommerceRepository {

    private final ProjectApiRoot apiRoot;

    @Override
    public Customer create(CommerceCustomer customer) {
        return apiRoot
                .customers()
                .create(CustomerDraft
                        .builder()
                        .email(customer.getEmail())
                        .password(customer.getPassword())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .companyName(customer.getCompanyName())
                        .build())
                .executeBlocking()
                .getBody()
                .getCustomer();
    }

    @Override
    public List<Customer> read(PagePaginationBuilder pageRequest) {
        return apiRoot
                .customers()
                .get()
                .executeBlocking()
                .getBody()
                .getResults();
    }

    @Override
    public Customer readById(String id) {
        return apiRoot
                .customers()
                .withId(id)
                .get()
                .executeBlocking()
                .getBody();
    }

    @Override
    public void delete(String id, long version) {
        apiRoot
                .customers()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();
    }

    @Override
    public long getProductVersion(String id) {
        return apiRoot
                .customers()
                .withId(id)
                .get()
                .executeBlocking()
                .getBody().getVersion();
    }
}
