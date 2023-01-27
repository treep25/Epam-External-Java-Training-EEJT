package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.handler.ResponseExceptionHandler;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.orders.controller.OrderController;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.model.OrderHateoasResponse;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.model.TagHateoasResponse;
import com.epam.esm.tag.service.TagService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    private MockMvc mvc;
    @Mock
    private TagService tagService;
    @InjectMocks
    private TagController tagController;

    private JacksonTester<Map<String, CollectionModel<?>>> jsonOrderTester;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private TagHateoasResponse hateoasResponse;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(tagController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    @SneakyThrows
    void createTest_ReturnCreatedTag_WhenAllOk() {
        // given
        Tag tag = Tag.builder().id(1L).name("qwerty").build();

        when(tagService.createTag(tag)).thenReturn(tag);
        when(hateoasResponse.getHateoasTagForCreating(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        CollectionModel<Tag> expected = CollectionModel.of(List.of(tag));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/tags").contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(tag)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("saved tag", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnServerError_WhenNameIncorrect() {
        // given
        Tag tag = Tag.builder().id(1L).name("123").build();

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/tags").contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(tag)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void read() {
    }

    @Test
    @SneakyThrows
    void readByIdTest() {
        // given
        Tag tag = Tag.builder().id(1L).name("123").build();

        when(tagService.getTagById(1L)).thenReturn(tag);
        when(hateoasResponse.getHateoasTagForReadingById(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        CollectionModel<Tag> expected = CollectionModel.of(List.of(tag));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/tags/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("tag", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void readByIdTest_ReturnServerError_WhenIdIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/tags/-13")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void getTheMostWidelyUsedTag() {
        // given
        Tag tag = Tag.builder().id(1L).name("123").build();

        when(tagService.getTheMostWidelyUsedTagOfUserOrder()).thenReturn(tag);
        when(hateoasResponse.getHateoasTagForGettingTheMostWidelyUsedTag(tag)).thenReturn(CollectionModel.of(List.of(tag)));
        CollectionModel<Tag> expected = CollectionModel.of(List.of(tag));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/tags/widely-used-tag")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonOrderTester.write(Map.of("the most widely used tag", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        // given
        long id = 1L;
        when(tagService.deleteTag(id)).thenReturn(true);

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                        .delete("/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnServerError_WhenIdIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                        .delete("/tags/-31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }
}