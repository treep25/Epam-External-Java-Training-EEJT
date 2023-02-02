package com.epam.esm.orders.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.orders.controller.OrderController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class OrderHateoasBuilderTest {

    @InjectMocks
    private OrderHateoasBuilder orderHateoasBuilder;

    @Mock
    private PagedResourcesAssembler<Order> representationModelAssembler;

    @Captor
    ArgumentCaptor<Link> linkCaptor;

    @Test
    void getHateoasOrderForReadingTest() {
        //given
        Order order = new Order();
        Page<Order> orders = new PageImpl<>(List.of(order));
        PagedModel<Order> allOrdersModel = mock(PagedModel.class);

        when(allOrdersModel.add(ArgumentMatchers.<Link>any())).thenReturn(allOrdersModel);

        when(representationModelAssembler.toModel(eq(orders), ArgumentMatchers.<RepresentationModelAssembler<Order, Order>>any())).thenReturn(allOrdersModel);

        //when
        PagedModel<Order> actual = orderHateoasBuilder.getHateoasOrderForReading(orders);

        //then
        assertEquals(actual, allOrdersModel);
        verify(actual, times(6)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create order");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get all gift-certificates");
        comparingLinks(linkCaptor.getAllValues().get(2).getRel().value(), "create gift-certificate");
        comparingLinks(linkCaptor.getAllValues().get(3).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(4).getRel().value(), "get tags");
        comparingLinks(linkCaptor.getAllValues().get(5).getRel().value(), "get the most widely used tag");

    }

    @Test
    void getHateoasOrderForCreatingTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        Order orderObj = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificateObj).cost(100).build();
        Order orderExpected = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificateObj).cost(100).build();
        CollectionModel<Order> expected = CollectionModel.of(List.of(orderExpected));
        orderExpected
                .getGiftCertificate()
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readById(orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "get gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(orderExpected.getGiftCertificate().getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readByCertificateNameOrByPartOfName(orderExpected.getGiftCertificate().getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));
        expected
                .add(linkTo(methodOn(OrderController.class)
                        .read(0, 20))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
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
        CollectionModel<Order> actual = orderHateoasBuilder.getHateoasOrderForCreating(orderObj);

        //then
        Assertions.assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasOrderForReadingByIdTest() {
        //given
        GiftCertificate giftCertificateObj = GiftCertificate.builder().id(1L).name("NamePlug").description(null).price(100).durationDays(100).tags(null).createDate(null).lastUpdateDate(null).build();
        Order orderObj = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificateObj).cost(100).build();
        Order orderExpected = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificateObj).cost(100).build();
        CollectionModel<Order> expected = CollectionModel.of(List.of(orderExpected));

        orderExpected
                .getGiftCertificate()
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readById(orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "get gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), orderExpected.getGiftCertificate().getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(orderExpected.getGiftCertificate().getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readByCertificateNameOrByPartOfName(orderExpected.getGiftCertificate().getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));
        expected
                .add(linkTo(methodOn(OrderController.class)
                        .create(0, 0))
                        .withRel(() -> "create order"))
                .add(linkTo(methodOn(OrderController.class)
                        .read(0, 20))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .read(0, 20))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
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
        CollectionModel<Order> actual = orderHateoasBuilder.getHateoasOrderForReadingById(orderObj);

        //then
        Assertions.assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    private void comparingLinks(String link1, String link2) {
        Assertions.assertEquals(link1, link2);
    }
}