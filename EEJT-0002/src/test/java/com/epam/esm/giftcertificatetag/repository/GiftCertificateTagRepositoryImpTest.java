package com.epam.esm.giftcertificatetag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateTagRepositoryImpTest {

    private EmbeddedDatabase db;
    private GiftCertificateTagRepository giftCertificateTagRepository;

    @BeforeEach
    public void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embedded_db/create-db.sql")
                .addScript("embedded_db/certificate_tag_repositories_test/insert-gift-certificate-tags.sql")
                .build();
        this.giftCertificateTagRepository = new GiftCertificateTagRepositoryImp(new NamedParameterJdbcTemplate(db).getJdbcTemplate());
    }

    @Test
    void getGiftCertificateTagsByTagNameTest_ReturnListOFCertificates() {
        //given
        String tagNameObj = "SomeName1";
        List<GiftCertificate> expected = List.of(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.getGiftCertificateTagsByTagName(tagNameObj);
        //then
        assertEquals(expected, actual);
    }

    @Test
    void getGiftCertificatesAndTagsByByPartOfNameTest_ReturnListOFCertificates() {

    }

    @Test
    void getGiftCertificatesAndTagsByNameTest_ReturnListOFCertificates() {

    }

    @Test
    void sortingAscDescByDateTestASC_ReturnListOFCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));


        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDate("ASC");
        //then
        assertEquals(expected, actual);
    }

    @Test
    void sortingAscDescByDateTestDESC_ReturnListOFCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDate("DESC");
        //then
        assertEquals(expected, actual);
    }

    @Test
    void sortingAscDescByDateAndByNameTestASCAndDESC_ReturnListOfGiftCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDateAndByName("ASC", "DESC");
        //then
        assertEquals(expected, actual);
    }

    @Test
    void sortingAscDescByDateAndByNameTestDESCAndASC_ReturnListOfGiftCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDateAndByName("DESC", "ASC");
        //then
        assertEquals(expected, actual);
    }

    @Test
    void sortingAscDescByDateAndByNameTestDESCAndDESC_ReturnListOfGiftCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDateAndByName("DESc", "DESC");
        //then
        assertEquals(expected, actual);
    }

    @Test
    void sortingAscDescByDateAndByNameTestASCAndASC_ReturnListOfGiftCertificates() {
        //given
        List<GiftCertificate> expected = new ArrayList<>();

        expected.add(new GiftCertificate().setId(2L).setName("Gift2").
                setTags(List.of(new Tag().setId(2L).setName("SomeName2"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2022-01-04T01:00Z").
                setLastUpdateDate("2022-01-04T01:00Z"));

        expected.add(new GiftCertificate().setId(1L).setName("GiftCertificate1").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate("2023-01-04T01:00Z").
                setLastUpdateDate("2023-01-04T01:00Z"));

        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.sortingAscDescByDateAndByName("ASC", "asc");
        //then
        assertEquals(expected, actual);
    }

    @AfterEach
    public void shutDown() {
        db.shutdown();
    }
}