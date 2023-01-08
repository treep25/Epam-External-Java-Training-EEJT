package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TagRepositoryImplTest {
    private EmbeddedDatabase db;
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embedded_db/create-db.sql")
                .addScript("embedded_db/tag_repositories_test/insert-tags.sql")
                .build();
        this.tagRepository = new TagRepositoryImpl(new NamedParameterJdbcTemplate(db).getJdbcTemplate());
    }

    @Test
    void createTagTest() {
        //given
        Tag testTagObj = new Tag().setName("testName");
        //when
        boolean actual = tagRepository.createTag(testTagObj);
        //then
        assertTrue(actual);
    }

    @Test
    void deleteTag_Return1WhenTagExists() {
        //given
        int testId = 1;
        //when
        boolean actual = tagRepository.deleteTag(testId);
        //then
        assertTrue(actual);

    }

    @Test
    void deleteTag_Return0_WhenNoSuchTag() {
        //given
        int testId = 1;
        //when
        boolean actual = tagRepository.deleteTag(testId);
        //then
        assertTrue(actual);

    }

    @Test
    void getAllTagsTest() {
        //given
        List<Tag> expectedTagList = List.of(new Tag().setId(1L).setName("SomeName1"), new Tag().setName("SomeName2").setId(2L));
        List<Tag> actualTagList = tagRepository.getAllTags();

        //then
        assertEquals(expectedTagList, actualTagList);
    }

    @Test
    void getTagById_ReturnTag() {
        //given
        int testId = 1;
        String expectedName = "SomeName1";
        //when
        List<Tag> actualTagList = tagRepository.getTagById(testId);
        Tag actualTag = actualTagList.get(0);
        //then
        assertNotNull(actualTag);
        assertEquals(testId, actualTag.getId());
        assertEquals(expectedName, actualTag.getName());
    }

    @Test
    void getTagById_ReturnEmpty_WhenNoSuchIdToGetTag() {
        //given
        int testId = 123;
        List<Tag> expectedTagList = List.of();
        //when
        List<Tag> actualTagList = tagRepository.getTagById(testId);
        //then
        assertEquals(actualTagList, expectedTagList);
    }

    @Test
    void getIdByTagTest() {
        //given
        long expectedId = 1L;
        //when
        long actualId = tagRepository.getIdByTag(new Tag().setName("SomeName1"));
        //then
        assertEquals(expectedId, actualId);
    }

    @AfterEach
    public void shutDown() {
        db.shutdown();
    }
}