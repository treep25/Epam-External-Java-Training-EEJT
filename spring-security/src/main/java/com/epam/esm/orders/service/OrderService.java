package com.epam.esm.orders.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableTransactionManagement
public class OrderService {


    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(long userId, long giftCertificateId) {
        log.info("Service receives gift-certificate ID and user ID for creating order" + "Transaction has been started");

        log.debug("Getting current gift-certificate " + giftCertificateId);
        GiftCertificate giftCertificateOrder = giftCertificateService.getCertificateById(giftCertificateId);
        log.debug("Getting current user " + userId);
        User userById = userService.getById(userId);

        log.debug("Creating order");
        Order order = new Order();
        order.setGiftCertificate(giftCertificateOrder);
        order.setCost(giftCertificateOrder.getPrice());

        log.debug("Saving current order");
        Order savedOrder = orderRepository.save(order);

        log.debug("Updating current user with this order");
        Set<Order> userOrder = userById.getOrders();
        userOrder.add(savedOrder);

        userService.updateUserOrder(userById);
        log.info("Transaction has been ended");

        log.debug("Service returns representation of current order");
        return savedOrder;
    }

    public Page<Order> getAll(int page, int size) {
        log.info("Service receives params for getting");

        PageRequest pageRequest = PageRequest.of(page, size);

        log.debug("Service returns representation of orders");
        return orderRepository.findAll(pageRequest);
    }

    public Order getOrderById(long id) {
        log.info("Service receives params for getting");
        log.debug("Service returns representation of orders");

        return orderRepository.findById(id).orElseThrow(
                () -> {
                    log.error("there are no orders with (id = " + id + ")");
                    return new ItemNotFoundException("there are no orders with (id = " + id + ")");
                });
    }
}
