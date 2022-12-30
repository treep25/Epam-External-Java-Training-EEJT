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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class TagRepositoryImplTest {
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
    void createTagTest() {
        //given
        long expectedId = 1L;
        Tag testTagObj = new Tag().setName("testName");
        //when
        int actual = tagRepository.createTag(testTagObj);
        //then
        assertEquals(expectedId, actual);
    }

    @Test
    void deleteTag_Return1WhenTagExists() {
        //given
        long expectedId = 1L;
        int testId = 1;
        //when
        int actual = tagRepository.deleteTag(testId);
        //then
        assertEquals(expectedId, actual);

    }

    @Test
    void deleteTag_Return0_WhenNoSuchTag() {
        //given
        long expectedId = 1L;
        int testId = 1;
        //when
        int actual = tagRepository.deleteTag(testId);
        //then
        assertEquals(expectedId, actual);

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