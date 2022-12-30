package com.epam.esm.giftcertficate.repository;

import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.tag.repository.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class GiftCertificateRepositoryImpTest {

    private EmbeddedDatabase db;
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("sql/tag_repositories_test/create-db.sql")
                .addScript("sql/tag_repositories_test/insert-tags.sql")
                .build();
        this.tagRepository = new TagRepositoryImpl(new NamedParameterJdbcTemplate(db).getJdbcTemplate());
    }

    @Test
    void getAllGiftCertificates() {
    }

    @Test
    void createCertificate() {
    }

    @Test
    void getCertificateById() {
    }

    @Test
    void deleteCertificate() {
    }

    @Test
    void updateGiftCertificate() {
    }

    @Test
    void getIdByGiftCertificate() {
    }

    @Test
    void isGiftCertificateExist() {
    }

    @Test
    void createGiftCertificateTagList() {
    }

    @AfterEach
    public void shutDown() {
        db.shutdown();
    }
}