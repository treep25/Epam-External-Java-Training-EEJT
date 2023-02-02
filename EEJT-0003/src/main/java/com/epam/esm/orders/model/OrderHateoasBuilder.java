package com.epam.esm.orders.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.orders.controller.OrderController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
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
                            .readById(order.getId()))
                            .withRel(() -> "get order"));

                    if (order.getGiftCertificate().getLinks().isEmpty()) {
                        order.getGiftCertificate()
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .readById(order.getGiftCertificate().getId()))
                                        .withRel(() -> "get gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .delete(order.getGiftCertificate().getId()))
                                        .withRel(() -> "delete gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updateCertificate(new GiftCertificate(), order.getGiftCertificate().getId()))
                                        .withRel(() -> "update gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updatePrice(order.getGiftCertificate().getId(), 0))
                                        .withRel(() -> "update gift-certificate`s price"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .readByCertificateNameOrByPartOfName(order.getGiftCertificate().getName(), 0, 20))
                                        .withRel(() -> "get all gift-certificates by name or by part of name"));
                    }
                    if (order.getGiftCertificate().getTags() != null) {
                        order.getGiftCertificate().getTags().forEach(tag -> {
                            if (tag.getLinks().isEmpty()) {
                                tag.add(linkTo(methodOn(TagController.class)
                                                .delete(tag.getId()))
                                                .withRel(() -> "delete tag"))
                                        .add(linkTo(methodOn(TagController.class)
                                                .readById(tag.getId()))
                                                .withRel(() -> "get tag"))
                                        .add(linkTo(methodOn(GiftCertificateController.class)
                                                .readByTagName(tag.getName(), 0, 20))
                                                .withRel(() -> "get gift-certificates by tag name"));
                            }
                        });
                    }
                    return order;
                });

        orders.add(linkTo(methodOn(OrderController.class)
                        .create(0, 0))
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

        if (order.getGiftCertificate().getLinks().isEmpty()) {
            order.getGiftCertificate()
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readById(order.getGiftCertificate().getId()))
                            .withRel(() -> "get gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .delete(order.getGiftCertificate().getId()))
                            .withRel(() -> "delete gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updateCertificate(new GiftCertificate(), order.getGiftCertificate().getId()))
                            .withRel(() -> "update gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updatePrice(order.getGiftCertificate().getId(), 0))
                            .withRel(() -> "update gift-certificate`s price"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readByCertificateNameOrByPartOfName(order.getGiftCertificate().getName(), 0, 20))
                            .withRel(() -> "get all gift-certificates by name or by part of name"));
        }
        if (order.getGiftCertificate().getTags() != null) {
            order.getGiftCertificate().getTags().forEach(tag -> {
                if (tag.getLinks().isEmpty()) {
                    tag.add(linkTo(methodOn(TagController.class)
                                    .delete(tag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(tag.getId()))
                                    .withRel(() -> "get tag"))
                            .add(linkTo(methodOn(GiftCertificateController.class)
                                    .readByTagName(tag.getName(), 0, 20))
                                    .withRel(() -> "get gift-certificates by tag name"));
                }
            });
        }
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

        if (order.getGiftCertificate().getLinks().isEmpty()) {
            order.getGiftCertificate()
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readById(order.getGiftCertificate().getId()))
                            .withRel(() -> "get gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .delete(order.getGiftCertificate().getId()))
                            .withRel(() -> "delete gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updateCertificate(new GiftCertificate(), order.getGiftCertificate().getId()))
                            .withRel(() -> "update gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updatePrice(order.getGiftCertificate().getId(), 0))
                            .withRel(() -> "update gift-certificate`s price"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readByCertificateNameOrByPartOfName(order.getGiftCertificate().getName(), 0, 20))
                            .withRel(() -> "get all gift-certificates by name or by part of name"));
        }
        if (order.getGiftCertificate().getTags() != null) {
            order.getGiftCertificate().getTags().forEach(tag -> {
                if (tag.getLinks().isEmpty()) {
                    tag.add(linkTo(methodOn(TagController.class)
                                    .delete(tag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(tag.getId()))
                                    .withRel(() -> "get tag"))
                            .add(linkTo(methodOn(GiftCertificateController.class)
                                    .readByTagName(tag.getName(), 0, 20))
                                    .withRel(() -> "get gift-certificates by tag name"));
                }
            });
        }
        orderById
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
        return orderById;
    }
}
