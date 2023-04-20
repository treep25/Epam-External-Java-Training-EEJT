package com.epam.esm.commercetools.customer.controller;

import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.customer.model.CommerceCustomer;
import com.epam.esm.commercetools.customer.service.CustomerCommerceService;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commerce/customers")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerCommerceController {

    private final CustomerCommerceService customerCommerceService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommerceCustomer customer) {
        return new ResponseEntity<>(customerCommerceService
                .create(customer), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        return ResponseEntity.ok(customerCommerceService
                .getAll(new PagePaginationBuilder(PageRequest.of(page, size))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        if (DataValidation.isCommerceIdValid(id)) {
            customerCommerceService.delete(id);

            return ResponseEntity.noContent().build();
        }
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

}
