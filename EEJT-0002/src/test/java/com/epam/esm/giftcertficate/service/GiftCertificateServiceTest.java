package com.epam.esm.giftcertficate.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.tag.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMock;
    @Mock
    private TagRepository tagRepositoryMock;
    @Mock
    private TagService tagServiceMock;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMock;

    @Test
    public void createCertificateTest_Return1IfCreated() {
        //given
        GiftCertificate giftCertificateTestObj = new GiftCertificate().setTags(List.of(new Tag().setName("1Tag")));
        List<Long> tagsIdList = List.of(1L);
        long certificateId = 1L;

        when(giftCertificateRepositoryMock.isGiftCertificateExist(giftCertificateTestObj)).thenReturn(false);
        when(giftCertificateRepositoryMock.createCertificate(giftCertificateTestObj)).thenReturn(true);

        when(tagServiceMock.isTagsExistOrElseCreate(giftCertificateTestObj.getTags())).thenReturn(true);
        when(tagServiceMock.getTagsIdByTags(giftCertificateTestObj.getTags())).thenReturn(tagsIdList);

        when(giftCertificateRepositoryMock.getIdByGiftCertificate(giftCertificateTestObj)).thenReturn(certificateId);
        when(giftCertificateRepositoryMock.createGiftCertificateTagRelationship(tagsIdList, certificateId)).thenReturn(true);

        //when
        boolean actual = giftCertificateServiceMock.createCertificate(giftCertificateTestObj);

        //then
        assertTrue(actual);
    }

    @Test
    public void createCertificateTest_Return0IfDidNotCreate() {
        //given
        GiftCertificate giftCertificateTestObj = new GiftCertificate().setTags(List.of(new Tag().setName("1Tag")));
        boolean expectedResult = true;

        //when
        when(giftCertificateRepositoryMock.isGiftCertificateExist(giftCertificateTestObj)).thenReturn(expectedResult);
        ServerException thrown = assertThrows(ServerException.class,
                () -> giftCertificateServiceMock.createCertificate(giftCertificateTestObj));

        //then
        assertEquals("Such certificate has already existed", thrown.getMessage());
    }

    @Test
    public void createCertificateTest_Return1IfCreated_WithOutTags() {
        //given
        GiftCertificate giftCertificateTestObj = new GiftCertificate();

        when(giftCertificateRepositoryMock.isGiftCertificateExist(giftCertificateTestObj)).thenReturn(false);
        when(giftCertificateRepositoryMock.createCertificate(giftCertificateTestObj)).thenReturn(true);

        //then
        assertTrue(giftCertificateServiceMock.createCertificate(giftCertificateTestObj));
    }

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
    public void deleteGiftCertificateTest_ReturnItemNotFoundException_WhenListIsEmpty() {
        //given
        long testObjId = 1L;

        when(giftCertificateRepositoryMock.deleteCertificate(testObjId)).thenReturn(false);

        //when

        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.deleteGiftCertificate(testObjId));
        //then
        assertEquals("There is no gift certificate with id= " + testObjId, thrown.getMessage());
    }

    @Test
    public void deleteGiftCertificateTest_Return1_WhenListIsNotEmpty() {
        //given
        long testObjId = 1L;

        when(giftCertificateRepositoryMock.deleteCertificate(testObjId)).thenReturn(true);

        //then
        assertTrue(giftCertificateServiceMock.deleteGiftCertificate(testObjId));
    }

    @Test
    void getGiftCertificateId() {
        //given
        long id = 1L;
        GiftCertificate giftCertificate = new GiftCertificate();
        //when
        when(giftCertificateRepositoryMock.getIdByGiftCertificate(giftCertificate)).thenReturn(id);

        //then
        assertEquals(id, giftCertificateServiceMock.getGiftCertificateId(giftCertificate));
    }
}