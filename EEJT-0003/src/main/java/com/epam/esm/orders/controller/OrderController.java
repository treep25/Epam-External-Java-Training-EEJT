package com.epam.esm.orders.controller;

import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{id}/{GCid}")
    public ResponseEntity<?> create(@PathVariable("id") long id, @PathVariable("GCid") long giftCertificateId) {
        Order savedOrder = orderService.createOrder(id, giftCertificateId);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        DataValidation.validatePageAndSizePagination(page, size);

        return ResponseEntity.ok(orderService.getAll(page, size));
    }
}
