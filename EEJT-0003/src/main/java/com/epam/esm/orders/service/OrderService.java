package com.epam.esm.orders.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableTransactionManagement
public class OrderService {


    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(long id, long giftCertificateId) {
        GiftCertificate giftCertificateOrder = giftCertificateService.getOneGiftCertificateById(giftCertificateId);
        User userById = userService.getById(id);

        Order order = new Order();
        order.setGiftCertificate(giftCertificateOrder);
        order.setCost(giftCertificateOrder.getPrice());

        Order savedOrder = orderRepository.save(order);

        Set<Order> userOrder = userById.getOrders();
        userOrder.add(savedOrder);

        userService.updateUserOrder(userById);

        return savedOrder;
    }

    public Page<Order> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return orderRepository.findAll(pageRequest);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("There are no orders with (id = " + id + ")"));
    }
}
