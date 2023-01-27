package com.epam.esm.orders.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepositoryMock;
    @InjectMocks
    private OrderService orderServiceMock;
    @Mock
    private GiftCertificateService giftCertificateServiceMock;
    @Mock
    private UserService userServiceMock;

    @Test
    void createOrderTest() {
        //given
        GiftCertificate giftCertificate = GiftCertificate.builder().price(1000).build();
        Set<Order> orderSet = new HashSet<>() {{
            add(new Order());
        }};
        User user = User.builder().orders(orderSet).build();
        Order orderExpected = Order.builder().cost(1000).giftCertificate(giftCertificate).build();

        when(giftCertificateServiceMock.getOneGiftCertificateById(1L)).thenReturn(giftCertificate);

        when(userServiceMock.getById(1L)).thenReturn(user);

        when(orderRepositoryMock.save(orderExpected)).thenReturn(orderExpected);
        //when
        Order orderActual = orderServiceMock.createOrder(1L, 1L);

        //then
        assertEquals(orderExpected, orderActual);
    }

    @Test
    void getAllTest() {
        //given
        Page<Order> allOrdersExpected = new PageImpl<>(List.of(new Order(), new Order()));
        PageRequest pageRequest = PageRequest.of(0, 5);
        when(orderRepositoryMock.findAll(pageRequest)).thenReturn(allOrdersExpected);

        //when
        Page<Order> allOrdersActual = orderServiceMock.getAll(0, 5);

        //then
        assertEquals(allOrdersExpected, allOrdersActual);
    }

    @Test
    void getOrderByIdTest_ReturnOrderById() {
        //given
        Order expectedOrder = new Order();
        when(orderRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedOrder));

        //when
        Order ordersByIdActual = orderServiceMock.getOrderById(1L);

        //then
        assertEquals(expectedOrder, ordersByIdActual);
    }

    @Test
    void getOrderByIdTest_ReturnItemNotFoundException_WhenThereAreNoById() {
        //given
        when(orderRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> orderServiceMock.getOrderById(1L));

        //then
        assertEquals("there are no orders with (id = " + 1 + ")", thrown.getMessage());
    }
}