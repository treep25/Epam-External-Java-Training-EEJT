package com.epam.esm.giftcertficate.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GiftCertificateHateoasResponseTest {
    @InjectMocks
    private GiftCertificateHateoasResponse giftCertificateHateoasResponse;

    @Mock
    private PagedResourcesAssembler<GiftCertificate> representationModelAssembler;

    @Captor
    ArgumentCaptor<Link> linkCaptor;

    @Test
    void getHateoasGiftCertificateForCreatingTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificateExpected));

        giftCertificateExpected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readById(giftCertificateExpected.getId()))
                        .withRel(() -> "get gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificateExpected.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), giftCertificateExpected.getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificateExpected.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificateExpected.getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));

        expected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"))
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));

        //when
        CollectionModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForCreating(giftCertificateObj);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasGiftCertificateForGettingAllTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingAll(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(7)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get all gift-certificates by tags and price");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates order by date");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "get all gift-certificates order by date and by name");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(6).getRel().value(), "get the most widely used tag");
    }

    @Test
    void getHateoasGiftCertificateForGettingOneTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificateExpected));
        giftCertificateExpected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificateExpected.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), giftCertificateExpected.getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificateExpected.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificateExpected.getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name ot by part of name"));

        expected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"))
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));

        //when
        CollectionModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingOne(giftCertificateObj);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasGiftCertificateForUpdateTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificateExpected));

        giftCertificateExpected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificateExpected.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificateExpected.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificateExpected.getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));
        expected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"))
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));

        //when
        CollectionModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForUpdate(giftCertificateObj);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasGiftCertificateForUpdatingPriceTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        GiftCertificate giftCertificateExpected = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        CollectionModel<GiftCertificate> expected = CollectionModel.of(List.of(giftCertificateExpected));

        giftCertificateExpected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificateExpected.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificateExpected.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificateExpected.getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));
        expected
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"))
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));

        //when
        CollectionModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForUpdatingPrice(giftCertificateObj);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasGiftCertificateForGettingByTagNameTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingByTagName(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(8)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "get all gift-certificates");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates by tags and price");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "get all gift-certificates order by date");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get all gift-certificates order by date and by name");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(6).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(7).getRel().value(), "get the most widely used tag");

    }

    @Test
    void getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfNameTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfName(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(8)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "get all gift-certificates");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates by tags and price");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "get all gift-certificates order by date");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get all gift-certificates order by date and by name");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(6).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(7).getRel().value(), "get the most widely used tag");
    }

    @Test
    void getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingGiftCertificatesSortedByDate(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(6)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get all gift-certificates by tags and price");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates order by date and by name");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "get the most widely used tag");
    }

    @Test
    void getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPriceTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPrice(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(6)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get all gift-certificates order by date");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates order by date and by name");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "get the most widely used tag");
    }

    @Test
    void getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByNameTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasResponse.getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByName(giftCertificates);

        //then
        assertEquals(actual, allGiftCertificatesModel);
        verify(actual, times(6)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get all gift-certificates by tags and price");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "get all gift-certificates order by date");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "get the most widely used tag");
    }

    private void comparingLinks(String link1, String link2) {
        assertEquals(link1, link2);
    }
}