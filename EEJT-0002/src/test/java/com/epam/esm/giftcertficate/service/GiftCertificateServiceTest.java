package com.epam.esm.giftcertficate.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMock;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMock;
    @InjectMocks
    private TagService tagServiceMock;

    @Test
    void getAllCertificatesTest() {
        //given
        List<GiftCertificate> expected = List.of(new GiftCertificate());

        //when
        when(giftCertificateRepositoryMock.getAllGiftCertificates()).thenReturn(expected);

        //then
        List<GiftCertificate> actual = giftCertificateServiceMock.getAllCertificates();
        assertEquals(expected, actual);
    }

    @Test
    void getAllCertificatesTest_ReturnItemNotFoundException_WhenListEmpty() {
        //given
        List<GiftCertificate> testObj = List.of();

        //when
        when(giftCertificateRepositoryMock.getAllGiftCertificates()).thenReturn(testObj);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.getAllCertificates());
        //then
        assertEquals("There are no gift certificates", thrown.getMessage());

    }

    @Test
    void getCertificateByIdTest() {
        //given
        List<GiftCertificate> expected = List.of(new GiftCertificate());
        long expectedId = 1L;
        //when
        when(giftCertificateRepositoryMock.getCertificateById(expectedId)).thenReturn(expected);
        List<GiftCertificate> actual = giftCertificateServiceMock.getCertificateById(expectedId);
        //then
        assertEquals(expected, actual);

    }

    @Test
    void getCertificateByIdTest_ReturnItemNotFoundException_WhenListEmpty() {
        //given
        List<GiftCertificate> expected = List.of();
        long expectedId = 1L;
        //when
        when(giftCertificateRepositoryMock.getCertificateById(expectedId)).thenReturn(expected);
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.getCertificateById(expectedId));

        //then
        assertEquals("There are no gift certificate with id= " + expectedId, thrown.getMessage());
    }

    @Test
    void updateGiftCertificateTest_ReturnListGiftCertificates_WhenAlIfComplete() {
        //given
        List<GiftCertificate> expected = List.of(new GiftCertificate().setTags(List.of(new Tag().setName("newTag"))));
        Map<String, String> updatesMap = Map.of("name", "newName");
        long testId = 1L;
        List<Tag> tagsObj = List.of(new Tag().setName("newTag"));

        //when
        when(giftCertificateRepositoryMock.updateGiftCertificate(testId, Optional.of(updatesMap))).thenReturn(1);
        when(tagServiceMock.getAllTagsByCertificateId(testId)).thenReturn(tagsObj);
//        when(tagServiceMock.isTagsExistOrElseCreate(List.of(new Tag().setName("newTag")))).thenReturn() //TODO

        //then


    }

    @Test
    void getGiftCertificateId() {
    }
}