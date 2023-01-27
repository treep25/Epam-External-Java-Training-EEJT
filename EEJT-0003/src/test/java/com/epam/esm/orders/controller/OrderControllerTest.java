package com.epam.esm.orders.controller;

import com.epam.esm.exceptionhandler.handler.ResponseExceptionHandler;
import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificateHateoasResponse;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.model.OrderHateoasResponse;
import com.epam.esm.orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    private MockMvc mvc;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;

    private JacksonTester<Map<String, CollectionModel<?>>> jsonOrderTester;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private OrderHateoasResponse hateoasResponse;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    @SneakyThrows
    void createTest_ReturnCreatedOrder_WhenAllOk() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("GfPlug").description("Plug").price(100).durationDays(10).tags(null).build();
        Order order = Order.builder().id(1L).cost(1000).giftCertificate(giftCertificate).purchaseDate(null).build();

        when(orderService.createOrder(1L, 1L)).thenReturn(order);
        when(hateoasResponse.getHateoasOrderForCreating(order)).thenReturn(CollectionModel.of(List.of(order)));
        CollectionModel<Order> expected = CollectionModel.of(List.of(order));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/orders/1/1").contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("order", expected)).getJson()
                , response.getContentAsString());

    }

    @Test
    @SneakyThrows
    void createTest_ReturnServerError_WhenIdIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/orders/-1/1").contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());

    }

    @Test
    @SneakyThrows
    void getByIdTest_ReturnOrder_WhenAllOk() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("GfPlug").description("Plug").price(100).durationDays(10).tags(null).build();
        Order order = Order.builder().id(1L).cost(1000).giftCertificate(giftCertificate).purchaseDate(null).build();

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(hateoasResponse.getHateoasOrderForReadingById(order)).thenReturn(CollectionModel.of(List.of(order)));
        CollectionModel<Order> expected = CollectionModel.of(List.of(order));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/orders/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("order", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void getByIdTest_ReturnServerError_WhenIdIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/orders/-41")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void getAll() {
    }
}