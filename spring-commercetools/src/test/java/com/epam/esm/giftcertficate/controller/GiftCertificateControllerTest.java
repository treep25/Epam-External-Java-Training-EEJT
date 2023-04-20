package com.epam.esm.giftcertficate.controller;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.role.Role;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GiftCertificateControllerTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static User ADMIN = User.builder().id(1L).name("ADMIN").role(Role.ADMIN).build();
    private final static Tag TAG = Tag.builder().id(1L).name("testObj").build();
    private final static User USER = User.builder().id(1L).name("USER").role(Role.USER).build();
    private final static GiftCertificate GIFT_CERTIFICATE = GiftCertificate.builder().id(1L).name("test").durationDays(123).price(123).description("123").build();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        giftCertificateRepository.save(GIFT_CERTIFICATE);
    }

    @Test
    @SneakyThrows
    void createTest_ReturnCreated_WhenAdmin() {
        mvc.perform(post("/api/v1/certificates")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(GiftCertificate.builder().description("test1").name("test1").price(1).durationDays(1).build())
                        ))
                .andExpect(status().isCreated());

    }

    @Test
    @SneakyThrows
    void createTest_ReturnServerError_WhenAdmin_WhenIncorrectData() {
        mvc.perform(post("/api/v1/certificates")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(GiftCertificate.builder().description("test").name("1").price(1).durationDays(1).build())
                        ))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @SneakyThrows
    void createTest_ReturnForbidden_WhenUser() {
        mvc.perform(post("/api/v1/certificates")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(GiftCertificate.builder().description("test").name("test").price(1).durationDays(1).build())
                        ))
                .andExpect(status().isForbidden());

    }

    @Test
    @SneakyThrows
    void createTest_ReturnUnauthorized_WhenNoAuth() {
        mvc.perform(post("/api/v1/certificates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }


    @Test
    @SneakyThrows
    void readTest_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readByIdTest_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void readByIdTest_ReturnServerError_ForAll_WhenIdLessThanZero() {
        mvc.perform(get("/api/v1/certificates/-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnOk_WhenAdmin() {
        mvc.perform(patch("/api/v1/certificates/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(Map.of("description","new description"))
                        ))
                .andExpect(status().isOk());

    }

    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnForbidden_WhenUser() {
        String description = "new description";
        mvc.perform(patch("/api/v1/certificates/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(description)
                        ))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnUnauthorized_WhenNoAuth() {
        String description = "new description";
        mvc.perform(patch("/api/v1/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(description)
                        ))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void updateCertificateTest_ReturnServerError_WhenParamIncorrect_WhenAdmin() {
        mvc.perform(patch("/api/v1/certificates/1")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(Map.of("price",-235))
                        ))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @SneakyThrows
    void deleteTest_ReturnServerError_WhenAdmin_WhenIdLessThanZero() {
        mvc.perform(delete("/api/v1/certificates/-123")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnNotFound_WhenAdmin_WhenNoSuchTag() {
        mvc.perform(delete("/api/v1/certificates/34")
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnUnauthorized_WhenNoAuth() {
        mvc.perform(delete("/api/v1/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void deleteTest_ReturnForbidden_WhenUser() {
        mvc.perform(delete("/api/v1/certificates/1")
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnGiftCertificate_WhenAllOK_WhenAdmin() {
        int price = 1234;
        mvc.perform(patch("/api/v1/certificates/update-price/1").content(
                                objectMapper.writeValueAsString(price)
                        )
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnGiftCertificate_WhenServerError_WhenAdmin() {
        int price = -1234;
        mvc.perform(patch("/api/v1/certificates/update-price/1").content(
                                objectMapper.writeValueAsString(price)
                        )
                        .with(user(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnGiftCertificate_ReturnForbidden_ForUser() {
        int price = 1234;
        mvc.perform(patch("/api/v1/certificates/update-price/1").content(
                                objectMapper.writeValueAsString(price)
                        )
                        .with(user(USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updatePriceTest_ReturnGiftCertificate_ReturnUnauthorized_WhenNoAuth() {
        int price = 1234;
        mvc.perform(patch("/api/v1/certificates/update-price/1").content(
                                objectMapper.writeValueAsString(price)
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @SneakyThrows
    void getGiftCertificatesByTagNameReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/tag-name?name=tagName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesByTagNameReturnServerError_WhenNameIncorrect() {
        mvc.perform(get("/api/v1/certificates/search/tag-name?name=123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesAndTagsByNameOrByPartOfNameTest_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/gift-certificate-name?name=newNameOrPartOfName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesAndTagsByNameOrByPartOfNameTest_ReturnServerError_WhenNameIncorrect() {
        mvc.perform(get("/api/v1/certificates/search/gift-certificate-name?name=355")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesSortedByDate_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/sort-date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesSortedByDate_ReturnServerError_WhenSortingTypeIncorrect_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/sort-date?sortDirection=EREW")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesByTagsAndPrice_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/tag-name/cost?tag1=ert&tag2=ert&price=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesByTagsAndPrice_ReturnServerError_WhenParamsIncorrect_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/tag-name/cost?tag1=ert&tag2=ert&price=-345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesSortedByDateAndByName_ReturnOk_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/sort-name-date?firstSortDirection=ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGiftCertificatesSortedByDateAndByName_ReturnServerError_WhenParamsIncorrect_ForAll() {
        mvc.perform(get("/api/v1/certificates/search/sort-name-date?firstSortDirection=ASDASD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}