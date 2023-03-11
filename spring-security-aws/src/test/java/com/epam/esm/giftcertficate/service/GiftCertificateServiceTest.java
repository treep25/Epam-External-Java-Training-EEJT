package com.epam.esm.giftcertficate.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMock;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMock;


    @Test
    void createGiftCertificateTest_ReturnSavedGiftCertificate_WhenNoTags() {
        //given
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().name("").build();
        when(giftCertificateRepositoryMock.existsByName(giftCertificateExpected.getName())).thenReturn(false);

        when(giftCertificateRepositoryMock.save(giftCertificateExpected)).thenReturn(giftCertificateExpected);
        //when
        GiftCertificate giftCertificateActual = giftCertificateServiceMock.createGiftCertificate(giftCertificateExpected);

        //then
        assertEquals(giftCertificateExpected, giftCertificateActual);
    }

    @Test
    void createGiftCertificateTest_ReturnServerException_WhenGiftCertificateExists() {
        //given
        GiftCertificate giftCertificate = GiftCertificate.builder().name("123").build();
        when(giftCertificateRepositoryMock.existsByName(giftCertificate.getName())).thenReturn(true);

        //when
        ServerException thrown = assertThrows(ServerException.class,
                () -> giftCertificateServiceMock.createGiftCertificate(giftCertificate));

        //then
        assertEquals("this gift certificate with (name = " + giftCertificate.getName() + ") has already existed", thrown.getMessage());
    }

    @Test
    void getAllTest() {
        //given
        Page<GiftCertificate> allGiftCertificatesExpected = new PageImpl<>(List.of(new GiftCertificate()));
        PageRequest pageRequest = PageRequest.of(0, 5);
        when(giftCertificateRepositoryMock.findAll(pageRequest)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> allGiftCertificatesActual = giftCertificateServiceMock.getAll(0, 5);

        //then
        assertEquals(allGiftCertificatesExpected, allGiftCertificatesActual);
    }

    @Test
    void getOneGiftCertificateByIdTest_ReturnCurrentGiftCertificate_WhenThereIsOneWithCurrentId() {
        //given
        GiftCertificate giftCertificateExpectedObj = new GiftCertificate();
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.of(giftCertificateExpectedObj));

        //when
        GiftCertificate giftCertificateActualObj = giftCertificateServiceMock.getCertificateById(1L);

        //then
        assertEquals(giftCertificateExpectedObj, giftCertificateActualObj);
    }

    @Test
    void getOneGiftCertificateByIdTest_ReturnItemNotFoundException_WhenThereIsNOGiftCertificateWithCurrentId() {
        //given
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.getCertificateById(1L));

        //then
        assertEquals("there are no gift certificate with (id = " + 1 + ")", thrown.getMessage());
    }

    @Test
    void updateGiftCertificateTest_ReturnServerException_WhenGiftCertificateExists() {
        //given
        Map<String, String> updatesMap = new HashMap<>() {{
            put("name", "Plug");
        }};
        when(giftCertificateRepositoryMock.existsByName("Plug")).thenReturn(true);

        //when
        ServerException thrown = assertThrows(ServerException.class,
                () -> giftCertificateServiceMock.updateCertificate(1L, null, updatesMap));

        //then
        assertEquals("the certificate with (name= " + updatesMap.get("name") + ") has already existed", thrown.getMessage());
    }

    @Test
    void updateGiftCertificateTest_UpdatedGiftCertificate_WhenThereAreNoTags() {
        //given
        Map<String, String> updatesMap = new HashMap<>() {{
            put("name", "Plug");
        }};
        GiftCertificate giftCertificate = GiftCertificate.builder().tags(null).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().name("Plug").tags(null).build();
        when(giftCertificateRepositoryMock.existsByName("Plug")).thenReturn(false);
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepositoryMock.save(giftCertificateExpected)).thenReturn(giftCertificateExpected);

        //when
        GiftCertificate giftCertificateActual = giftCertificateServiceMock.updateCertificate(1L, null, updatesMap);

        //then
        assertEquals(giftCertificateExpected, giftCertificateActual);
    }

    @Test
    void updateGiftCertificateTest_UpdatedGiftCertificate_WhenTagsAreEmpty() {
        //given
        Map<String, String> updatesMap = new HashMap<>() {{
            put("name", "Plug");
        }};

        GiftCertificate giftCertificate = GiftCertificate.builder().tags(Set.of()).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().name("Plug").tags(Set.of()).build();
        when(giftCertificateRepositoryMock.existsByName("Plug")).thenReturn(false);
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepositoryMock.save(giftCertificateExpected)).thenReturn(giftCertificateExpected);

        //when
        GiftCertificate giftCertificateActual = giftCertificateServiceMock.updateCertificate(1L, Set.of(), updatesMap);

        //then
        assertEquals(giftCertificateExpected, giftCertificateActual);
    }

    @Test
    void updatePriceTest_ReturnUpdatedGiftCertificate_WhenContainsById() {
        //given
        GiftCertificate giftCertificateBefore = GiftCertificate.builder().price(123).build();
        GiftCertificate giftCertificateAfter = GiftCertificate.builder().price(100).build();
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(giftCertificateBefore));
        when(giftCertificateRepositoryMock.save(giftCertificateAfter)).thenReturn(giftCertificateBefore);

        //when
        GiftCertificate giftCertificateActual = giftCertificateServiceMock.updatePrice(1L, 100);

        //then
        assertEquals(giftCertificateAfter, giftCertificateActual);
    }

    @Test
    void updatePriceTest_ReturnItemNotFoundException_WhenDoesNotContainById() {
        //given
        when(giftCertificateRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.updatePrice(1L, 100));

        //then
        assertEquals("there are no gift certificate with (id = " + 1 + ")", thrown.getMessage());
    }

    @Test
    void deleteGiftCertificate_ReturnTrue_WhenWasDeleted() {
        //given
        when(giftCertificateRepositoryMock.existsById(1L)).thenReturn(true);
        //when
        doNothing().when(giftCertificateRepositoryMock).deleteById(1L);

        giftCertificateServiceMock.deleteCertificate(1L);
        //then
        verify(giftCertificateRepositoryMock,times(1)).deleteById(1L);
    }

    @Test
    void deleteGiftCertificate_ReturnItemNotFoundException_WhenWasNotExisted() {
        //given
        when(giftCertificateRepositoryMock.existsById(1L)).thenReturn(false);

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> giftCertificateServiceMock.deleteCertificate(1L));

        //then
        assertEquals("there are no gift certificate with (id = " + 1 + ")", thrown.getMessage());
    }

    @Test
    void getGiftCertificatesByTagNameTest() {
        //given
        List<GiftCertificate> allGiftCertificatesExpected = List.of(new GiftCertificate());
        when(giftCertificateRepositoryMock.getAllGiftCertificatesByTagName("tagNamePlug", 0, 5)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> byTag = giftCertificateServiceMock.getCertificatesByTagName("tagNamePlug", 0, 5);

        //then
        assertEquals(List.of(new GiftCertificate()), byTag.get().collect(Collectors.toList()));
    }

    @Test
    void getGiftCertificatesByTagsAndPriceTest() {
        //given
        List<GiftCertificate> allGiftCertificatesExpected = List.of(new GiftCertificate());
        when(giftCertificateRepositoryMock.getGiftCertificatesByTagsAndPrice("tagNamePlug", "tagNamePlug1", 1, 0, 5)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> byTagsAndPrice = giftCertificateServiceMock.getCertificatesByTagsAndPrice("tagNamePlug", "tagNamePlug1", 1, 0, 5);

        //then
        assertEquals(List.of(new GiftCertificate()), byTagsAndPrice.get().collect(Collectors.toList()));

    }

    @Test
    void getGiftCertificatesByNameOrByPartOfNameTest() {
        //given
        List<GiftCertificate> allGiftCertificatesExpected = List.of(new GiftCertificate());
        when(giftCertificateRepositoryMock.getAllGiftCertificateByPartOfName("plug%", 0, 5)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> byPartOfName = giftCertificateServiceMock.getCertificatesByNameOrByPartOfName("plug", 0, 5);

        //then
        assertEquals(List.of(new GiftCertificate()), byPartOfName.get().collect(Collectors.toList()));
    }

    @Test
    void getGiftCertificatesSortedByDateTest() {
        //given
        Page<GiftCertificate> allGiftCertificatesExpected = new PageImpl<>(List.of(new GiftCertificate()));
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        when(giftCertificateRepositoryMock.findAll(pageRequest)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> sortedByDateDescActual = giftCertificateServiceMock.getCertificatesSortedByDate("DESC", 0, 5);

        //then
        assertEquals(allGiftCertificatesExpected, sortedByDateDescActual);
    }

    @Test
    void getGiftCertificatesSortedByDateAndByNameTest() {
        //given
        Page<GiftCertificate> allGiftCertificatesExpected = new PageImpl<>(List.of(new GiftCertificate()));
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.valueOf("asc".toUpperCase(Locale.ROOT)), "name")
                .and(Sort.by(Sort.Direction.valueOf("Desc".toUpperCase(Locale.ROOT)), "createDate")));
        when(giftCertificateRepositoryMock.findAll(pageRequest)).thenReturn(allGiftCertificatesExpected);

        //when
        Page<GiftCertificate> sortedByDateAndNameDescAscActual = giftCertificateServiceMock.getGiftCertificatesSortedByDateAndByName("asc", "Desc", 0, 5);

        //then
        assertEquals(allGiftCertificatesExpected, sortedByDateAndNameDescAscActual);
    }
}