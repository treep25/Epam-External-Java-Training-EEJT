package com.epam.esm.orders.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.model.OrderHateoasResponse;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final OrderHateoasResponse orderHateoasResponse = new OrderHateoasResponse();
    private final PagedResourcesAssembler<Order> representationModelAssembler;

    @PostMapping("/{userId}/{giftCertificateId}")
    public ResponseEntity<?> create(@PathVariable("userId") long userId,
                                    @PathVariable("giftCertificateId") long giftCertificateId) {
        if (DataValidation.moreThenZero(userId)) {
            if (DataValidation.moreThenZero(giftCertificateId)) {

                Order savedOrder = orderService.createOrder(userId, giftCertificateId);

                CollectionModel<Order> collectionModelOrder = orderHateoasResponse.getHateoasOrderForCreating(savedOrder);

                return ResponseEntity.ok(Map.of("order", collectionModelOrder));
            }
            throw new ServerException("The gift-certificate ID is not valid: id = " + giftCertificateId);
        }
        throw new ServerException("The user ID is not valid: id = " + userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            Order orderById = orderService.getOrderById(id);

            CollectionModel<Order> collectionModelOrder = orderHateoasResponse.getHateoasOrderForReadingById(orderById);

            return ResponseEntity.ok(Map.of("order", collectionModelOrder));
        }
        throw new ServerException("The user ID is not valid: id = " + id);

    }

    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        Page<Order> allOrders = orderService.getAll(page, size);

        PagedModel<Order> pagedModelOrders = orderHateoasResponse
                .getHateoasOrderForReading(allOrders, representationModelAssembler);

        return ResponseEntity.ok(Map.of("orders", pagedModelOrders));
    }
}
