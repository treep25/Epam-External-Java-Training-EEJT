package com.epam.esm.orders.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.jacoco.Generated;
import com.epam.esm.orders.controller.OrderController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserHateoasBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Generated
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderHateoasBuilder {

    private final PagedResourcesAssembler<Order> representationModelAssembler;

    public PagedModel<Order> getHateoasOrderForReading(Page<Order> pagedOrders) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<Order> orders = representationModelAssembler
                .toModel(pagedOrders, order -> {
                    order.add(linkTo(methodOn(OrderController.class)
                            .readById(new User(),order.getId()))
                            .withRel(() -> "get order"));

                    UserHateoasBuilder.builderForGettingAll(order);
                    return order;
                });

        orders.add(linkTo(methodOn(OrderController.class)
                        .create(new User(), 0))
                        .withRel(() -> "create order"))
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

        return orders;
    }

    public CollectionModel<Order> getHateoasOrderForCreating(Order order) {
        log.info("Building HATEOAS collection-model entity");

        CollectionModel<Order> savedOrder = CollectionModel.of(List.of(order));

        UserHateoasBuilder.builderForGettingAll(order);
        savedOrder
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

        return savedOrder;
    }

    public CollectionModel<Order> getHateoasOrderForReadingById(Order order) {
        log.info("Building HATEOAS collection-model entity");

        CollectionModel<Order> orderById = CollectionModel.of(List.of(order));

        UserHateoasBuilder.builderForGettingAll(order);
        orderById
                .add(linkTo(methodOn(OrderController.class)
                        .create(new User(), 0))
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
        return orderById;
    }
}
