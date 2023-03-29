package com.epam.esm.authentication.controller;

import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @Autowired
    private UserRepository userRepository;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        userRepository.save(User.builder().name("wet").id(1L).role(Role.USER).password(passwordEncoder.encode("qwe123")).build());
    }

    @Test
    @SneakyThrows
    void registerTest_ReturnOk_ForAll() {
        RegisterRequest request = RegisterRequest.builder().username("wet1").password(passwordEncoder.encode("qwe123")).build();
        mvc.perform(post("/api/v1/auth/register").content(
                            objectMapper.writeValueAsString(request)
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void registerTest_ReturnServerError_WhenPasswordNoValid_ForAll() {
        RegisterRequest request = RegisterRequest.builder().username("wet1").password("12").build();
        mvc.perform(post("/api/v1/auth/register").content(
                                objectMapper.writeValueAsString(request)
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @SneakyThrows
    void authenticateTest_ReturnServerError_WhenLoginOrPasswordIncorrect() {
        AuthenticationRequest request = AuthenticationRequest.builder().username("wet").password(passwordEncoder.encode("12")).build();
        mvc.perform(post("/api/v1/auth/register").content(
                                objectMapper.writeValueAsString(request)
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}