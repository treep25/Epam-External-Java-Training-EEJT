package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.tag.repository.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateRepositoryImpTest {

    private EmbeddedDatabase db;
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    public void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embedded_db/create-db.sql")
                .addScript("embedded_db/gift_certificate_test/insert-gift-certificates.sql")
                .build();
        this.giftCertificateRepository = new GiftCertificateRepositoryImp(new NamedParameterJdbcTemplate(db).getJdbcTemplate());
    }

    @Test
    void getAllGiftCertificatesTest_ReturnListOfAllCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        //when
        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"), new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDateString("2023-01-04T01:00Z").
                setLastUpdateDateString("2023-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(2L).setName("GiftCertificate2").
                setTags(List.of()).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDateString("2022-01-04T01:00Z").
                setLastUpdateDateString("2022-01-04T01:00Z"));

        List<GiftCertificate> actual = giftCertificateRepository.getAllGiftCertificates();
        //then
        assertEquals(expected, actual);
    }

    @Test
    void createCertificateTest_ReturnTrue() {
        //given
        GiftCertificate testObj = new GiftCertificate().setName("qwerty").
                setDescription("description1").
                setPrice(121).
                setDuration(1323);

        //then
        assertTrue(giftCertificateRepository.createCertificate(testObj));
    }

    @Test
    void getCertificateByIdTest_Return_GiftCertificate() {
        //given
        List<GiftCertificate> expected = List.of(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"), new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDateString("2023-01-04T01:00Z").
                setLastUpdateDateString("2023-01-04T01:00Z"));
        List<GiftCertificate> actual = giftCertificateRepository.getCertificateById(1);
        //then
        assertEquals(expected, actual);
    }

    @Test
    void deleteCertificateTest_ReturnTrue() {
        //then
        assertTrue(giftCertificateRepository.deleteCertificate(1));
    }

    @Test
    void updateGiftCertificateTest_ReturnGiftCertificate() {
        //given
        String descriptionUpdated = "q";
        int priceUpdated = 124444;

        boolean actual = giftCertificateRepository.updateGiftCertificate(1L,
                Optional.of(Map.of("description", descriptionUpdated, "price", String.valueOf(priceUpdated))));
        //then
        assertTrue(actual);
    }

    @Test
    void getIdByGiftCertificateTest_ReturnIdByCertificate() {
        //given
        long expected = 2;
        GiftCertificate giftCertificate = new GiftCertificate().setName("GiftCertificate2").
                setDescription("description").
                setPrice(12).
                setDuration(123);
        long actual = giftCertificateRepository.getIdByGiftCertificate(giftCertificate);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void isGiftCertificateExistTest_ReturnTrueIfExist() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate().setName("GiftCertificate2").
                setDescription("description").
                setPrice(12).
                setDuration(123);
        boolean actual = giftCertificateRepository.isGiftCertificateExist(giftCertificate);

        //then
        assertTrue(actual);
    }

    @Test
    void isGiftCertificateExistTest_ReturnFalseIfExist() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate().setName("GiftCert2312ificate2").
                setDescription("description").
                setPrice(12).
                setDuration(123);
        boolean actual = giftCertificateRepository.isGiftCertificateExist(giftCertificate);

        //then
        assertFalse(actual);
    }

    @Test
    void deleteGiftCertificateTagRelationshipTest_ReturnTrue() {
        //given
        long giftCertificateId = 1;
        List<Long> tagIds = List.of(1L, 2L);
        //then
        assertTrue(giftCertificateRepository.deleteGiftCertificateTagRelationship(tagIds, giftCertificateId));
    }

    @Test
    void createGiftCertificateTagRelationshipTest_ReturnTrue() {
        //given
        long giftCertificateId = 2;
        List<Long> tagIds = List.of(1L);
        //then
        assertTrue(giftCertificateRepository.createGiftCertificateTagRelationship(tagIds, giftCertificateId));
    }

    @AfterEach
    public void shutDown() {
        db.shutdown();
    }
}