package com.epam.esm.commercetools.customer.service;

import com.commercetools.api.models.customer.Customer;
import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.customer.model.CommerceCustomer;
import com.epam.esm.commercetools.customer.repository.CustomerCommerceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableTransactionManagement
public class CustomerCommerceService {

    private final CustomerCommerceRepository customerCustomerCommerceRepository;

    public Customer create(CommerceCustomer customer) {

        return customerCustomerCommerceRepository.create(customer);
    }

    public Page<Customer> getAll(PagePaginationBuilder pageRequest) {
        log.info("Service receives params for getting all");

        log.debug("Service returns all gift-certificates");
        return new PageImpl<>(customerCustomerCommerceRepository.read(pageRequest));
    }

    public void delete(String id) {

        customerCustomerCommerceRepository.delete(id,
                customerCustomerCommerceRepository.getProductVersion(id));
    }
}
