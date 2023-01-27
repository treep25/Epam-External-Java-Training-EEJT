package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepositoryMock;
    @InjectMocks
    private TagService tagServiceMock;

    @Test
    void createTagTest_ReturnCreatedTag() {
        //given
        Tag tagExpected = new Tag();
        tagExpected.setName("Qwerty");
        when(tagRepositoryMock.isTagExistsByName(tagExpected.getName())).thenReturn(false);
        when(tagRepositoryMock.save(tagExpected)).thenReturn(tagExpected);

        //when
        Tag tagActual = tagServiceMock.createTag(tagExpected);

        //then
        assertEquals(tagExpected, tagActual);
    }

    @Test
    void createTagTest_ReturnItemNotFoundException_WhenTagNameExist() {
        //given
        Tag tagExpected = new Tag();
        tagExpected.setName("Qwerty");
        when(tagRepositoryMock.isTagExistsByName(tagExpected.getName())).thenReturn(true);

        //when
        ServerException thrown = assertThrows(ServerException.class,
                () -> tagServiceMock.createTag(tagExpected));

        //then
        assertEquals("this tag with (name = " + tagExpected.getName() + ") has already existed", thrown.getMessage());
    }

    @Test
    void isTagNotExistsByNameTestReturnTrue() {
        //given
        when(tagRepositoryMock.isTagExistsByName("Tag")).thenReturn(false);

        //then
        assertTrue(tagServiceMock.isTagNotExistsByName("Tag"));

    }

    @Test
    void isTagNotExistsByNameTestReturnFalse() {
        //given
        when(tagRepositoryMock.isTagExistsByName("Tag")).thenReturn(true);

        //then
        assertFalse(tagServiceMock.isTagNotExistsByName("Tag"));

    }

    @Test
    void getAllTagsTest() {
        //given
        Page<Tag> allTagsExpected = new PageImpl<>(List.of(new Tag(), new Tag()));
        PageRequest pageRequest = PageRequest.of(0, 5);
        when(tagRepositoryMock.findAll(pageRequest)).thenReturn(allTagsExpected);

        //when
        Page<Tag> allTagsActual = tagServiceMock.getAllTags(0, 5);

        //then
        assertEquals(allTagsExpected, allTagsActual);
    }

    @Test
    void getTagByIdTest_ReturnTag_WhenThereAreById() {
        //given
        Tag expectedTag = new Tag();
        when(tagRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedTag));

        //when
        Tag tagByIdActual = tagServiceMock.getTagById(1L);

        //then
        assertEquals(expectedTag, tagByIdActual);
    }

    @Test
    void getTagByIdTest_ReturnItemNotFoundException_WhenThereAreNoById() {
        //given
        when(tagRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.getTagById(1L));

        //then
        assertEquals("there are no tags with (ID = " + 1 + ")", thrown.getMessage());
    }

    @Test
    void deleteTagTest_ReturnTrue_WhenWasDeleted() {
        //given
        when(tagRepositoryMock.existsById(1L)).thenReturn(true);

        //when
        boolean actual = tagServiceMock.deleteTag(1L);

        //then
        assertTrue(actual);
        verify(tagRepositoryMock).deleteById(any());
    }

    @Test
    void deleteTagTest_ReturnItemNotFoundException_WhenWasNotExist() {
        //given
        when(tagRepositoryMock.existsById(1L)).thenReturn(false);

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.deleteTag(1L));

        //then
        assertEquals("there is no tag with (id = " + 1L + ")", thrown.getMessage());
    }

    @Test
    void getTagIdByTagTest_ReturnLongId_WhenContain() {
        //given
        when(tagRepositoryMock.getIdByTagName("plugName")).thenReturn(12L);

        //when
        long idActual = tagServiceMock.getTagIdByTag(Tag.builder().name("plugName").build());

        //then
        assertEquals(12L, idActual);
    }

    @Test
    void getTheMostWidelyUsedTagOfUserOrderTest() {
        //given
        Tag widelyUsedExpected = Tag.builder().name("blank").build();
        when(tagRepositoryMock.getTheMostWidelyUsedTag()).thenReturn(widelyUsedExpected);

        //when
        Tag widelyUsedActual = tagServiceMock.getTheMostWidelyUsedTagOfUserOrder();

        //then
        assertEquals(widelyUsedExpected, widelyUsedActual);
    }

    @Test
    void verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificateTest() {
        //given
        Set<Tag> tagsExpected = Set.of(Tag.builder().name("Plug").build());
        when(tagRepositoryMock.isTagExistsByName("Plug")).thenReturn(false);
        when(tagRepositoryMock.saveAll(tagsExpected)).thenReturn(tagsExpected.stream().toList());

        //when
        Set<Tag> tagsActual = tagServiceMock.verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(tagsExpected);

        //then
        assertEquals(tagsExpected, tagsActual);
    }
}