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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateTagRepositoryImpTest {

    private EmbeddedDatabase db;
    private GiftCertificateTagRepository giftCertificateTagRepository;

    @BeforeEach
    public void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("sql/certificate_tag_repositories_test/create-db.sql")
                .addScript("sql/certificate_tag_repositories_test/insert-gift-certificate-tags.sql")
                .build();
        this.giftCertificateTagRepository = new GiftCertificateTagRepositoryImp(new NamedParameterJdbcTemplate(db).getJdbcTemplate());
    }

    @Test
    void getGiftCertificateTagsByTagName() {
        //given
        String tagNameObj = "SomeName1";
        List<GiftCertificate> expected = List.of(new GiftCertificate().setName("").
                setTags(List.of(new Tag().setId(1L).setName("SomeName1"))).
                setDescription("description").
                setPrice(12).
                setDuration(123).
                setCreateDate(new java.util.Date()).
                setLastUpdateDate(new java.util.Date()));
        //when
        List<GiftCertificate> actual = giftCertificateTagRepository.getGiftCertificateTagsByTagName(tagNameObj);//TODO
        //then
        assertEquals(expected, actual);
    }

    @Test
    void getGiftCertificatesAndTagsByNameOrByPartOfName() {
    }

    @Test
    void sortingAscDescByDate() {
    }

    @Test
    void sortingAscDescByDateAndByName() {
    }

    @AfterEach
    public void shutDown() {
        db.shutdown();
    }
}