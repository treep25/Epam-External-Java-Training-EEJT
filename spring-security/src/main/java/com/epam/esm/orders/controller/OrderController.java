package com.epam.esm.orders.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.model.OrderHateoasBuilder;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.user.model.User;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final OrderHateoasBuilder orderHateoasBuilder;

    @PostMapping("/{giftCertificateId}")
    public ResponseEntity<?> create(@AuthenticationPrincipal User user,
                                    @PathVariable("giftCertificateId") long giftCertificateId) {

        log.debug("Validation request model User ID " + user.getId());
        if (DataValidation.moreThenZero(user.getId())) {
            log.debug("Validation request model gift-certificate ID " + giftCertificateId);
            if (DataValidation.moreThenZero(giftCertificateId)) {

                Order savedOrder = orderService.createOrder(user.getId(), giftCertificateId);
                log.debug("Receive order");

                CollectionModel<Order> collectionModelOrder = orderHateoasBuilder.getHateoasOrderForCreating(savedOrder);
                log.debug("Return Hateoas model of current order");

                return new ResponseEntity<>(Map.of("order", collectionModelOrder), HttpStatus.CREATED);
            }
            log.error("The gift-certificate ID is not valid: id = " + giftCertificateId);
            throw new ServerException("the gift-certificate ID is not valid: id = " + giftCertificateId);
        }
        log.error("The user ID is not valid: id = " + user.getId());
        throw new ServerException("the user ID is not valid: id = " + user.getId());
    }

    @PreAuthorize("#user.ordersIds.contains(#id) or #user.role.name().equals('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@AuthenticationPrincipal User user, @PathVariable("id") long id) {
        log.debug("Validation request model order ID " + id);

        if (DataValidation.moreThenZero(id)) {

            Order orderById = orderService.getOrderById(user, id);
            log.debug("Receive order");

            CollectionModel<Order> collectionModelOrder = orderHateoasBuilder.getHateoasOrderForReadingById(orderById);
            log.debug("Return Hateoas model of current order");

            return ResponseEntity.ok(Map.of("order", collectionModelOrder));
        }
        log.error("The user ID is not valid: id = " + id);
        throw new ServerException("the user ID is not valid: id = " + id);
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model params");

        DataValidation.validatePageAndSizePagination(page, size);

        Page<Order> allOrders = orderService.getAll(page, size);
        log.debug("Receive all orders");

        PagedModel<Order> pagedModelOrders = orderHateoasBuilder
                .getHateoasOrderForReading(allOrders);
        log.debug("Return Hateoas model of all orders");

        return ResponseEntity.ok(Map.of("orders", pagedModelOrders));
    }
}
