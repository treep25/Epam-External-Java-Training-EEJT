package com.epam.esm.giftcertficate.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GiftCertificateHateoasBuilderTest {
    @InjectMocks
    private GiftCertificateHateoasBuilder giftCertificateHateoasBuilder;

    @Mock
    private PagedResourcesAssembler<GiftCertificate> representationModelAssembler;

    @Captor
    ArgumentCaptor<Link> linkCaptor;



    @Test
    void getHateoasGiftCertificateForGettingAllTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingAll(giftCertificates);

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
    void getHateoasGiftCertificateForGettingByTagNameTest() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(giftCertificate));
        PagedModel<GiftCertificate> allGiftCertificatesModel = mock(PagedModel.class);

        when(allGiftCertificatesModel.add(ArgumentMatchers.<Link>any())).thenReturn(allGiftCertificatesModel);

        when(representationModelAssembler.toModel(eq(giftCertificates), ArgumentMatchers.<RepresentationModelAssembler<GiftCertificate, GiftCertificate>>any())).thenReturn(allGiftCertificatesModel);

        //when
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingByTagName(giftCertificates);

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
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfName(giftCertificates);

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
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingGiftCertificatesSortedByDate(giftCertificates);

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
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPrice(giftCertificates);

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
        PagedModel<GiftCertificate> actual = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByName(giftCertificates);

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