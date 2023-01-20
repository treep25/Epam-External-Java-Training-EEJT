package com.epam.esm.orders.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.model.OrderHateoasResponse;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
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

        log.debug("Validation request model User ID");
        if (DataValidation.moreThenZero(userId)) {
            log.debug("Validation request model gift-certificate ID");
            if (DataValidation.moreThenZero(giftCertificateId)) {

                Order savedOrder = orderService.createOrder(userId, giftCertificateId);
                log.debug("Receive order");

                CollectionModel<Order> collectionModelOrder = orderHateoasResponse.getHateoasOrderForCreating(savedOrder);
                log.debug("Return Hateoas model of current order");

                return ResponseEntity.ok(Map.of("order", collectionModelOrder));
            }
            log.error("The gift-certificate ID is not valid: id = " + giftCertificateId);
            throw new ServerException("the gift-certificate ID is not valid: id = " + giftCertificateId);
        }
        log.error("The user ID is not valid: id = " + userId);
        throw new ServerException("the user ID is not valid: id = " + userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        log.debug("Validation request model order ID");

        if (DataValidation.moreThenZero(id)) {

            Order orderById = orderService.getOrderById(id);
            log.debug("Receive order");

            CollectionModel<Order> collectionModelOrder = orderHateoasResponse.getHateoasOrderForReadingById(orderById);
            log.debug("Return Hateoas model of current order");

            return ResponseEntity.ok(Map.of("order", collectionModelOrder));
        }
        log.error("The user ID is not valid: id = " + id);
        throw new ServerException("the user ID is not valid: id = " + id);
    }

    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model params");

        DataValidation.validatePageAndSizePagination(page, size);

        Page<Order> allOrders = orderService.getAll(page, size);
        log.debug("Receive all orders");

        PagedModel<Order> pagedModelOrders = orderHateoasResponse
                .getHateoasOrderForReading(allOrders, representationModelAssembler);
        log.debug("Return Hateoas model of all orders");

        return ResponseEntity.ok(Map.of("orders", pagedModelOrders));
    }
}
