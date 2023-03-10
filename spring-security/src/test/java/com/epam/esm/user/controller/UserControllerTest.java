package com.epam.esm.user.controller;

import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private UserRepository repository;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    private final static User ADMIN = User.builder().id(1L).name("ADMIN").role(Role.ADMIN).build();
    private final static User USER = User.builder().id(1L).name("USER").role(Role.USER).build();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        repository.save(User.builder().id(1L).password("password").role(Role.USER).name("name").build());
    }

    @Test
    @SneakyThrows
    void readAll_ReturnUnauthorized() {
        mvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void readAll_ReturnOk_WhenAdmin() {
        mvc.perform(get("/api/v1/users")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void readAll_ReturnForbidden_WhenUser() {
        mvc.perform(get("/api/v1/users")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void readByIdTest_WithoutToken_ReturnUnauthorized() {
        mvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    public void readByIdTest_WithTokenAdmin_ReturnOk() {
        mvc.perform(get("/api/v1/users/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void readByIdTest_WithTokenUser_ReturnOk_WhenIdUserEqualsIdThatUserWantsToGet() {
        mvc.perform(get("/api/v1/users/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void readByIdTest_WithTokenUser_ReturnForbidden_WhenIdUserNotEqualsIdThatUserWantsToGet() {
        mvc.perform(get("/api/v1/users/2")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnServerError_WhenIdIncorrect() {
        mvc.perform(get("/api/v1/users/-21")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}