package com.epam.esm.giftcertificatetag.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertificatetag.repository.GiftCertificateTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateTagServiceTest {
    @Mock
    private GiftCertificateTagRepository giftCertificateTagRepositoryMock;
    @InjectMocks
    private GiftCertificateTagService giftCertificateTagServiceMock;

    @Test
    void getGiftCertificatesByTagName() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObjExpected = List.of(new GiftCertificate());
        String tagNamePlug = "TagNamePlug";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificateTagsByTagName(tagNamePlug)).thenReturn(listGiftCertificateTagsObjExpected);

        //then

        assertEquals(listGiftCertificateTagsObjExpected, giftCertificateTagServiceMock.getGiftCertificatesByTagName(tagNamePlug));
    }

    @Test
    void getGiftCertificatesByTagName_ReturnItemNotFoundException_WhenGiftCertificatesTagListIsEmpty() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObj = List.of();
        String tagNamePlug = "TagNamePlug";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificateTagsByTagName(tagNamePlug)).thenReturn(listGiftCertificateTagsObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateTagServiceMock.getGiftCertificatesByTagName(tagNamePlug));

        //then
        assertEquals("There are no certificates with tag name = " + tagNamePlug, thrown.getMessage());
    }

    @Test
    void getGiftCertificatesAndTagsByNameOrByPartOfName() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObjExpected = List.of(new GiftCertificate());
        String tagNamePlug = "TagNamePlug";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesAndTagsByNameOrByPartOfName(tagNamePlug)).thenReturn(listGiftCertificateTagsObjExpected);

        //then

        assertEquals(listGiftCertificateTagsObjExpected, giftCertificateTagServiceMock.getGiftCertificatesAndTagsByNameOrByPartOfName(tagNamePlug));
    }


    @Test
    void getGiftCertificatesAndTagsByNameOrByPartOfName_ReturnItemNotFoundException_WhenGiftCertificatesTagByNameListIsEmpty() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObj = List.of();
        String tagNamePLug = "TagNameStub";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesAndTagsByNameOrByPartOfName(tagNamePLug)).thenReturn(listGiftCertificateTagsObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateTagServiceMock.getGiftCertificatesAndTagsByNameOrByPartOfName(tagNamePLug));

        //then
        assertEquals("There are no gift certificates with name or starting with  =" + tagNamePLug, thrown.getMessage());
    }

    @Test
    void sortingAscDescByDate() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObjExpected = List.of(new GiftCertificate());
        String sortingMethodPlug = "ASC";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesSortedByDate(sortingMethodPlug)).thenReturn(listGiftCertificateTagsObjExpected);

        //then

        assertEquals(listGiftCertificateTagsObjExpected, giftCertificateTagServiceMock.getGiftCertificatesSortedByDate(sortingMethodPlug));
    }


    @Test
    void sortingAscDescByDate_ReturnItemNotFoundException_WhenGiftCertificatesTagSortingListIsEmpty() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObj = List.of();
        String sortingMethodPlug = "ASC";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesSortedByDate(sortingMethodPlug)).thenReturn(listGiftCertificateTagsObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateTagServiceMock.getGiftCertificatesSortedByDate(sortingMethodPlug));

        //then
        assertEquals("There are no tags and certificates", thrown.getMessage());
    }

    @Test
    void sortingAscDescByDateAndByName() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObjExpected = List.of(new GiftCertificate());
        String sortingMethodPlug = "DESC";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesSortedByDateAndByName(sortingMethodPlug, sortingMethodPlug)).thenReturn(listGiftCertificateTagsObjExpected);

        //then

        assertEquals(listGiftCertificateTagsObjExpected, giftCertificateTagServiceMock.getGiftCertificatesSortedByDateAndByName(sortingMethodPlug, sortingMethodPlug));

    }

    @Test
    void sortingAscDescByDateAndByName_ReturnItemNotFoundException_WhenGiftCertificatesTagSortingByDateAndNameListIsEmpty() {
        //given
        List<GiftCertificate> listGiftCertificateTagsObj = List.of();
        String sortingMethodPlug = "DESC";

        //when
        when(giftCertificateTagRepositoryMock.getGiftCertificatesSortedByDateAndByName(sortingMethodPlug, sortingMethodPlug)).thenReturn(listGiftCertificateTagsObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateTagServiceMock.getGiftCertificatesSortedByDateAndByName(sortingMethodPlug, sortingMethodPlug));

        //then
        assertEquals("There are no tags and certificates", thrown.getMessage());
    }
}