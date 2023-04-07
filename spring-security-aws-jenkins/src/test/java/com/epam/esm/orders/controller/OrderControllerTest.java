package com.epam.esm.orders.controller;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.orders.model.Order;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private final static User ADMIN = User.builder().id(1L).name("ADMIN").role(Role.ADMIN).build();
    private final static GiftCertificate GIFT_CERTIFICATE = GiftCertificate.builder().id(1L).name("test").durationDays(123).price(123).description("123").build();
    private final static User USER = User.builder().id(1L).name("USER").role(Role.USER).orders(Set.of(Order.builder().id(1L).giftCertificate(GIFT_CERTIFICATE).cost(1).build())).build();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        userRepository.save(ADMIN);
        userRepository.save(USER);
        giftCertificateRepository.save(GIFT_CERTIFICATE);
    }

    @Test
    @SneakyThrows
    void createTest_ReturnUnauthorized_WhenTokenNotPresent() {
        mvc.perform(post("/api/v1/orders/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnServerError_WhenIdLessThanZero() {
        mvc.perform(post("/api/v1/orders/-234")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnOk_WhenAdmin() {
        mvc.perform(post("/api/v1/orders/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnOk_WhenUser() {
        mvc.perform(post("/api/v1/orders/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnOk_WhenAdmin() {
        mvc.perform(get("/api/v1/orders/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnNotFound_WhenAdmin() {
        mvc.perform(get("/api/v1/orders/123")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnOk_WhenUserHasThisOrder() {
        mvc.perform(get("/api/v1/orders/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnForbidden_WhenUserHasNotThisOrder() {
        mvc.perform(get("/api/v1/orders/23")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnUnauthorized_WhenNoToken() {
        mvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnServerError_WhenIdIncorrect() {
        mvc.perform(get("/api/v1/orders/-123")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
    @Test
    @SneakyThrows
    void readTest_ReturnOk_WhenAdmin() {
        mvc.perform(get("/api/v1/orders")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void readTest_ReturnForbidden_WhenUser() {
        mvc.perform(get("/api/v1/orders")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void readTest_ReturnUnauthorized_WhenNotAuth() {
        mvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}