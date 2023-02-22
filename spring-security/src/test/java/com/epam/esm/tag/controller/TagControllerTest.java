package com.epam.esm.tag.controller;

import com.epam.esm.role.Role;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagControllerTest {

    @Autowired
    private TagRepository tagRepository;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static User ADMIN = User.builder().id(1L).name("ADMIN").role(Role.ADMIN).build();
    private final static Tag TAG = Tag.builder().id(1L).name("testObj").build();
    private final static User USER = User.builder().id(1L).name("USER").role(Role.USER).build();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        tagRepository.saveAndFlush(TAG);
    }

    @Test
    @SneakyThrows
    void createTest_ReturnCreated_WhenAdmin() {
        mvc.perform(post("/api/v1/tags")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(Tag.builder().name("test").build())
                        ))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnServerError_WhenAdmin_WhenWrongData() {
        mvc.perform(post("/api/v1/tags")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(Tag.builder().name("1").build())
                        ))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnForbidden_WhenUser() {
        mvc.perform(post("/api/v1/tags")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnUnauthorized_WhenNoAuth() {
        mvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @SneakyThrows
    void readTest_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    @SneakyThrows
    void readByIdTest_ReturnServerError_WhenIdLessThanZero() {
        mvc.perform(get("/api/v1/tags/-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnNotFound() {
        mvc.perform(get("/api/v1/tags/435")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnNoContent_WhenAdmin() {
        mvc.perform(delete("/api/v1/tags/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnServerError_WhenAdmin_WhenIdLessThanZero() {
        mvc.perform(delete("/api/v1/tags/-123")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnNotFound_WhenAdmin_WhenNoSuchTag() {
        mvc.perform(delete("/api/v1/tags/34")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnUnauthorized_WhenNoAuth() {
        mvc.perform(delete("/api/v1/tags/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnForbidden_WhenUser() {
        mvc.perform(delete("/api/v1/tags/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}