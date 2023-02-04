package com.epam.esm.user.controller;

import com.epam.esm.exceptionhandler.handler.ResponseExceptionHandler;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserHateoasBuilder;
import com.epam.esm.user.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private JacksonTester<Map<String, CollectionModel<?>>> jsonOrderTester;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserHateoasBuilder userHateoasBuilder;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    void read() {
    }

    @Test
    @SneakyThrows
    void readByIdTest() {
        // given
        User user = User.builder().id(1L).name("NamePlug").build();

        when(userService.getById(1L)).thenReturn(user);
        when(userHateoasBuilder.getHateoasUserForReadingById(user)).thenReturn(CollectionModel.of(List.of(user)));
        CollectionModel<User> expected = CollectionModel.of(List.of(user));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("user", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnServerError_WhenIdIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/-21")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }
}