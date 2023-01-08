package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepositoryMock;
    @InjectMocks
    private TagService tagServiceMock;


    @Test
    void createTag_ReturnIllegalArgumentException_WhenNameAlreadyExists() {
        //given
        String tagNamePlug = "Existing name";
        boolean expectedResult = true;

        //when
        when(tagRepositoryMock.isTagWithNameExists(tagNamePlug)).thenReturn(expectedResult);
        ServerException thrown = assertThrows(ServerException.class,
                () -> tagServiceMock.createTag(new Tag().setName(tagNamePlug)));

        //then
        assertEquals(thrown.getMessage(), "This tag has already existed");
    }

    @Test
    void createTag_Return1() {
        //given
        Tag tagPlug = new Tag().setName("TagNamePlug");
        boolean expectedResult = false;

        //when
        when(tagRepositoryMock.isTagWithNameExists(tagPlug.getName())).thenReturn(expectedResult);
        when(tagServiceMock.createTag(tagPlug)).thenReturn(true);

        //then
        assertTrue(tagServiceMock.createTag(tagPlug));
    }

    @Test
    void getAllTags_ReturnListTags() {
        //given
        List<Tag> expected = List.of(new Tag().setId(1L).setName("test1"), new Tag().setId(2L).setName("test2"));

        //when
        when(tagRepositoryMock.getAllTags()).thenReturn(expected);

        //then
        assertEquals(expected, tagServiceMock.getAllTags());
    }

    @Test
    void getTagById_ReturnListOfOneTag() {
        //given
        long idPlug = 132;
        Tag tagPlug = new Tag().setName("tagNamePlug");
        List<Tag> expected = List.of(tagPlug);

        //when
        when(tagRepositoryMock.getTagById(idPlug)).thenReturn(expected);

        //then
        assertEquals(expected, tagServiceMock.getTagById(idPlug));
    }

    @Test
    void getTagById_ReturnItemNotFoundException_WhenListOfTagsEmpty() {
        //given
        long idPlug = 132;
        List<Tag> listTagsObj = List.of();
        //when
        when(tagRepositoryMock.getTagById(idPlug)).thenReturn(listTagsObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.getTagById(idPlug));
        //then
        assertEquals("There are no tags with id= " + idPlug, thrown.getMessage());
    }

    @Test
    void deleteTag_Return1() {
        //given
        long idPlug = 123;
        int resultExpected = 1;

        when(tagRepositoryMock.deleteTag(idPlug)).thenReturn(true);

        //when

        //then
        assertTrue(tagServiceMock.deleteTag(idPlug));
    }

    @Test
    void deleteTag_ReturnItemNotFoundException_WhenNoTagToDelete() {
        //given
        long idPlug = 123;

        when(tagRepositoryMock.deleteTag(idPlug)).thenReturn(false);

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> tagServiceMock.deleteTag(idPlug));

        //then
        assertEquals("There is no tag with id= " + idPlug, thrown.getMessage());

    }

    @Test
    void isTagByNameExists() {
        //given
        boolean expected = true;
        String namePlug = "testName1";

        //when
        when(tagRepositoryMock.isTagWithNameExists(namePlug)).thenReturn(expected);

        //then
        assertTrue(tagServiceMock.isTagByNameExists(namePlug));
    }

    @Test
    void getTagIdByTag() {
        //given
        Tag tagPlug = new Tag().setName("TagPlug");
        long expected = 123;

        //when
        when(tagRepositoryMock.getIdByTag(tagPlug)).thenReturn(expected);

        //then
        assertEquals(expected, tagServiceMock.getTagIdByTag(tagPlug));
    }

    @Test
    void getAllTagsByCertificateId() {
        //given
        List<Tag> expected = List.of(new Tag().setName("1"), new Tag().setName("2"));
        long idPlug = 123;

        //when
        when(tagRepositoryMock.getAllTagsByCertificateId(idPlug)).thenReturn(expected);

        //then
        assertEquals(expected, tagServiceMock.getAllTagsByCertificateId(idPlug));
    }
}