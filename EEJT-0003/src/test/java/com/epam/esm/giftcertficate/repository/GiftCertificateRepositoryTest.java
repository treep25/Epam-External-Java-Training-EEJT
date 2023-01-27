package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GiftCertificateRepositoryTest {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;

    private GiftCertificate giftCertificateTest1;

    private GiftCertificate giftCertificateTest2;

    private GiftCertificate giftCertificateTest3;

    private final Tag tagTest1 = Tag.builder().name("tagtag").build();
    private final Tag tagTest2 = Tag.builder().name("tag").build();
    private final Tag tagTest3 = Tag.builder().name("tag4").build();
    private final Tag tagTest4 = Tag.builder().name("tag6").build();
    private final Tag tagTest5 = Tag.builder().name("tag2").build();
    private final Tag tagTest6 = Tag.builder().name("tag1").build();

    @BeforeEach
    public void initEntities() {
        tagRepository.saveAll(List.of(tagTest1, tagTest2, tagTest3, tagTest4, tagTest5, tagTest6));

        this.giftCertificateTest1 = GiftCertificate.builder()
                .name("gc1")
                .description("gc1")
                .price(102)
                .durationDays(12).tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("tagtag")).name("tagtag").build())).build();

        this.giftCertificateTest2 = GiftCertificate.builder()
                .name("gc2")
                .description("gc2")
                .price(102)
                .durationDays(12)
                .tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("tag")).name("tag").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag4")).name("tag4").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag6")).name("tag6").build())).build();

        this.giftCertificateTest3 = GiftCertificate.builder()
                .name("Look")
                .description("gc3")
                .price(102)
                .durationDays(12).tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("tag")).name("tag").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag2")).name("tag2").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag1")).name("tag1").build())).build();

        giftCertificateRepository.saveAll(List.of(giftCertificateTest1, giftCertificateTest2, giftCertificateTest3));
    }

    @Test
    void isGiftCertificateExistByNameTest_ReturnTrueIfExists() {

        assertTrue(giftCertificateRepository.isGiftCertificateExistByName("gc1"));
    }

    @Test
    void isGiftCertificateExistByNameTest_ReturnFalseIfNotExists() {

        assertFalse(giftCertificateRepository.isGiftCertificateExistByName("notEx"));
    }

    @Test
    void getAllGiftCertificatesByTagName() {
        List<GiftCertificate> allGiftCertificatesByNameList = List.of(giftCertificateTest2, giftCertificateTest3);

        assertEquals(allGiftCertificatesByNameList, giftCertificateRepository.getAllGiftCertificatesByTagName("tag", 0, 5));
    }

    @Test
    void getAllGiftCertificateByPartOfName() {

        assertEquals(List.of(giftCertificateTest1, giftCertificateTest2), giftCertificateRepository.getAllGiftCertificateByPartOfName("gc%", 0, 5));
    }

    @Test
    void getGiftCertificatesByTagsAndPrice() {

        assertEquals(List.of(giftCertificateTest2), giftCertificateRepository.getGiftCertificatesByTagsAndPrice("tag", "tag4", 100, 0, 5));

    }

    @AfterEach
    public void deleteEntities() {
        tagRepository.deleteAll();
        giftCertificateRepository.deleteAll();
    }
}