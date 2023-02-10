package com.epam.esm.user.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.orders.controller.OrderController;
import com.epam.esm.orders.model.Order;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.user.controller.UserController;
import com.epam.esm.user.dto.UserDTO;
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
public class UserHateoasBuilder {

    private final PagedResourcesAssembler<UserDTO> representationModelAssembler;

    public PagedModel<UserDTO> getHateoasUserForReading(Page<UserDTO> pagedUsers) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<UserDTO> users = representationModelAssembler
                .toModel(pagedUsers, user -> {
                    user.add(linkTo(methodOn(UserController.class)
                            .readById(new User(),user.getId()))
                            .withRel(() -> "get user"));

                    builderForCreate(user);
                    return user;
                });

        users.add(linkTo(methodOn(OrderController.class)
                .read(0, 20))
                .withRel(() -> "get all orders"));

        return users;
    }

    private void builderForCreate(UserDTO user) {
        user.getOrders().forEach(order -> {
            order.add(linkTo(methodOn(OrderController.class)
                            .readById(new User(),order.getId()))
                            .withRel(() -> "get order"))
                    .add(linkTo(methodOn(OrderController.class)
                            .create(new User(), 0))
                            .withRel(() -> "create order"));

            builderForGettingAll(order);
        });
    }

    public static void builderForGettingAll(Order order) {
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
    }

    public CollectionModel<UserDTO> getHateoasUserForReadingById(UserDTO userDTO) {
        log.info("Building HATEOAS collection-model entity");

        builderForCreate(userDTO);


        return CollectionModel.of(List.of(userDTO))
                .add(linkTo(methodOn(UserController.class)
                        .read(0, 20))
                        .withRel(() -> "get all users"))
                .add(linkTo(methodOn(OrderController.class)
                        .read(0, 20))
                        .withRel(() -> "get all orders"));
    }
}