package com.epam.esm.giftcertficate.controller;

import com.epam.esm.exceptionhandler.handler.ResponseExceptionHandler;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificateHateoasResponse;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.tag.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {

    private MockMvc mvc;

    @Mock
    private GiftCertificateService giftCertificateService;
    @InjectMocks
    private GiftCertificateController giftCertificateController;

    private JacksonTester<Map<String, CollectionModel<?>>> jsonGiftCertificateJacksonTester;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private GiftCertificateHateoasResponse hateoasResponse;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(giftCertificateController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    @SneakyThrows
    void createTest_ReturnCreatedGiftCertificate_WhenAllOk() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("GfPlug").description("Plug").price(100).durationDays(10).tags(null).build();

        when(giftCertificateService.createGiftCertificate(giftCertificate)).thenReturn(giftCertificate);
        when(hateoasResponse.getHateoasGiftCertificateForCreating(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificate));

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/certificates").contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(giftCertificate)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonGiftCertificateJacksonTester.write(Map.of("gift-certificate", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void createTest_ReturnException_When500() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("").description("Plug").price(100).durationDays(10).tags(null).build();


        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/certificates").contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(giftCertificate)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());

    }


    @Test
    @SneakyThrows
    void readTest_ReturnModel_WhenAllOK() {
        
    }

    @SneakyThrows
    @Test
    void readByIdTest() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L).name("GfPlug").description("Plug").price(100).durationDays(10).tags(Set.of(new Tag().builder().id(1L).name("tagPlug").build())).build();
        when(giftCertificateService.getOneGiftCertificateById(1L)).thenReturn(giftCertificate);
        when(hateoasResponse.getHateoasGiftCertificateForGettingOne(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificate));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/certificates/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateJacksonTester.write(Map.of("gift-certificate", expected)).getJson()
                , response.getContentAsString());

    }

    @SneakyThrows
    @Test
    void readByIdTest_ReturnServerException_WhenIdNotCorrect() {
        // when
        Map<String, ?> responseExpected = Map.of("HTTP Status", "" + "500 INTERNAL_SERVER_ERROR", "response body", Map.of("message", "The Gift Certificate ID is not valid: id = -23"));
        MockHttpServletResponse response = mvc.perform(
                        get("/certificates/-23")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(responseExpected)
                , response.getContentAsString());

    }

    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnUpdatedGiftCertificate_WhenAllOk() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("GfPlug").build();
        when(giftCertificateService.updateGiftCertificate(1L, null, Map.of("name", "GfPlug"))).thenReturn(giftCertificate);
        when(hateoasResponse.getHateoasGiftCertificateForUpdate(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificate));

        // when
        MockHttpServletResponse response = mvc.perform(
                        patch("/certificates/1").
                                contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(giftCertificate)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateJacksonTester.write(Map.of("gift-certificate", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnServerError_WhenParamIncorrect() {
        // given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("123").build();

        // when
        MockHttpServletResponse response = mvc.perform(
                        patch("/certificates/1").
                                contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(giftCertificate)
                                )
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        // given
        long giftCertificateId = 1L;
        when(giftCertificateService.deleteGiftCertificate(giftCertificateId)).thenReturn(true);

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                        .delete("/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnServerError_WhenIdIsIncorrect() {
        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                        .delete("/certificates/-32")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnGiftCertificate_WhenAllOK() {
        // given
        int price = 1000;
        long id = 1L;
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L).name("GfPlug").price(1000).build();
        when(giftCertificateService.updatePrice(id, price)).thenReturn(giftCertificate);
        when(hateoasResponse.getHateoasGiftCertificateForUpdatingPrice(giftCertificate)).thenReturn(CollectionModel.of(List.of(giftCertificate)));
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificate));

        // when
        MockHttpServletResponse response = mvc.perform(
                        patch("/certificates/update-price/1").
                                contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(price))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonGiftCertificateJacksonTester.write(Map.of("gift-certificate", expected)).getJson()
                , response.getContentAsString());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnServerError_WhenPriceOrIdIncorrect() {
        // given
        int price = -123;
        // when
        MockHttpServletResponse response = mvc.perform(
                        patch("/certificates/update-price/-123").
                                contentType(MediaType.APPLICATION_JSON).content(
                                        objectMapper.writeValueAsString(price))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void getGiftCertificatesByTagName() {
    }

    @Test
    void getGiftCertificatesAndTagsByNameOrByPartOfName() {
    }

    @Test
    void getGiftCertificatesSortedByDate() {
    }

    @Test
    void getGiftCertificatesByTagsAndPrice() {
    }

    @Test
    void getGiftCertificatesSortedByDateAndByName() {
    }
}