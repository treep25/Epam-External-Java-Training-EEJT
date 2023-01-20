package com.epam.esm.giftcertficate.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.model.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
public class GiftCertificateHateoasResponse {
    public CollectionModel<GiftCertificate> getHateoasGiftCertificateForCreating(GiftCertificate giftCertificate) {
        log.info("Building HATEOAS collection-model entity");

        getTagsLinks(giftCertificate.getTags());

        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                        getLinksForGiftCertificateReadAndCreate(giftCertificate)))
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
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificate(giftCertificateCollectionModel);

        return giftCertificateCollectionModel;
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingAll(Page<GiftCertificate> giftCertificates,
                                                                              PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> allGiftCertificatesModel = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinks(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreate(giftCertificate);
                });

        allGiftCertificatesModel
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
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificatePagination(allGiftCertificatesModel);

        return allGiftCertificatesModel;
    }

    public CollectionModel<GiftCertificate> getHateoasGiftCertificateForGettingOne(GiftCertificate giftCertificate) {
        log.info("Building HATEOAS collection-model entity");

        getTagsLinks(giftCertificate.getTags());

        CollectionModel<GiftCertificate> currentGiftCertificateModel = CollectionModel.of(List.of(
                        giftCertificate
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .delete(giftCertificate.getId()))
                                        .withRel(() -> "delete gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updateCertificate(new GiftCertificate(), giftCertificate.getId()))
                                        .withRel(() -> "update gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updatePrice(giftCertificate.getId(), 0))
                                        .withRel(() -> "update gift-certificate`s price"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificate.getName(), 0, 20))
                                        .withRel(() -> "get all gift-certificates by name ot by part of name"))))

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
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificate(currentGiftCertificateModel);

        return currentGiftCertificateModel;
    }

    public CollectionModel<GiftCertificate> getHateoasGiftCertificateForUpdate(GiftCertificate giftCertificate) {
        log.info("Building HATEOAS collection-model entity");

        getTagsLinks(giftCertificate.getTags());

        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                        giftCertificate
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .delete(giftCertificate.getId()))
                                        .withRel(() -> "delete gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updatePrice(giftCertificate.getId(), 0))
                                        .withRel(() -> "update gift-certificate`s price"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificate.getName(), 0, 20))
                                        .withRel(() -> "get all gift-certificates by name or by part of name"))))
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
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificate(giftCertificateCollectionModel);

        return giftCertificateCollectionModel;
    }

    public CollectionModel<GiftCertificate> getHateoasGiftCertificateForUpdatingPrice(GiftCertificate giftCertificate) {
        log.info("Building HATEOAS collection-model entity");

        getTagsLinks(giftCertificate.getTags());

        CollectionModel<GiftCertificate> giftCertificateCollectionModel = CollectionModel.of(List.of(
                        giftCertificate
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .delete(giftCertificate.getId()))
                                        .withRel(() -> "delete gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .updateCertificate(new GiftCertificate(), giftCertificate.getId()))
                                        .withRel(() -> "update gift-certificate"))
                                .add(linkTo(methodOn(GiftCertificateController.class)
                                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificate.getName(), 0, 20))
                                        .withRel(() -> "get all gift-certificates by name ot by part of name"))))
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
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificate(giftCertificateCollectionModel);

        return giftCertificateCollectionModel;
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingByTagName(Page<GiftCertificate> giftCertificates,
                                                                                    PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> pagedModelGiftCertificates = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinksWithOutByTagName(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreate(giftCertificate);
                });

        getPageModelLinks(pagedModelGiftCertificates);

        getTagsLinksForGiftCertificatePagination(pagedModelGiftCertificates);

        return pagedModelGiftCertificates;
    }

    private void getPageModelLinks(PagedModel<GiftCertificate> pagedModel) {
        pagedModel
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
                        .withRel(() -> "get all gift-certificates order by date and by name"));
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfName(Page<GiftCertificate> giftCertificates,
                                                                                                               PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> pagedModelGiftCertificates = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinks(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreateWithOitGettingByPartOfName(giftCertificate);
                });

        getPageModelLinks(pagedModelGiftCertificates);

        getTagsLinksForGiftCertificatePagination(pagedModelGiftCertificates);

        return pagedModelGiftCertificates;
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingGiftCertificatesSortedByDate(Page<GiftCertificate> giftCertificates,
                                                                                                       PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> allGiftCertificatesModel = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinks(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreate(giftCertificate);
                });

        allGiftCertificatesModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificatePagination(allGiftCertificatesModel);

        return allGiftCertificatesModel;
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPrice(Page<GiftCertificate> giftCertificates,
                                                                                                         PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> allGiftCertificatesModel = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinks(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreate(giftCertificate);
                });

        allGiftCertificatesModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDateAndByName("ASC", "ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date and by name"));

        getTagsLinksForGiftCertificatePagination(allGiftCertificatesModel);

        return allGiftCertificatesModel;
    }

    public PagedModel<GiftCertificate> getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByName(Page<GiftCertificate> giftCertificates,
                                                                                                                PagedResourcesAssembler<GiftCertificate> representationModelAssembler) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<GiftCertificate> allGiftCertificatesModel = representationModelAssembler
                .toModel(giftCertificates, giftCertificate -> {
                    getTagsLinks(giftCertificate.getTags());

                    return getLinksForGiftCertificateReadAndCreate(giftCertificate);
                });

        allGiftCertificatesModel
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(new GiftCertificate()))
                        .withRel(() -> "create gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagsAndPrice("", "", 0, 0, 20))
                        .withRel(() -> "get all gift-certificates by tags and price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByDate("ASC", 0, 20))
                        .withRel(() -> "get all gift-certificates order by date"));

        getTagsLinksForGiftCertificatePagination(allGiftCertificatesModel);

        return allGiftCertificatesModel;
    }

    private void getTagsLinks(Set<Tag> tags) {
        if (tags != null) {
            tags.forEach(tag -> {
                if (tag.getLinks().isEmpty()) {
                    tag.add(linkTo(methodOn(TagController.class)
                                    .delete(tag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(tag.getId()))
                                    .withRel(() -> "get tag"))
                            .add(linkTo(methodOn(GiftCertificateController.class)
                                    .getGiftCertificatesByTagName(tag.getName(), 0, 20))
                                    .withRel(() -> "get gift-certificates by tag name"));
                }
            });
        }
    }

    private void getTagsLinksWithOutByTagName(Set<Tag> tags) {
        if (tags != null) {
            tags.forEach(tag -> {
                if (tag.getLinks().isEmpty()) {
                    tag.add(linkTo(methodOn(TagController.class)
                                    .delete(tag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(tag.getId()))
                                    .withRel(() -> "get tag"));
                }
            });
        }
    }

    private void getTagsLinksForGiftCertificate(CollectionModel<GiftCertificate> collectionModelGiftCertificate) {
        collectionModelGiftCertificate
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));
    }

    private void getTagsLinksForGiftCertificatePagination(PagedModel<GiftCertificate> pageModelGiftCertificate) {
        pageModelGiftCertificate
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag"));
    }

    private GiftCertificate getLinksForGiftCertificateReadAndCreate(GiftCertificate giftCertificate) {
        return giftCertificate
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readById(giftCertificate.getId()))
                        .withRel(() -> "get gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificate.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), giftCertificate.getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesAndTagsByNameOrByPartOfName(giftCertificate.getName(), 0, 20))
                        .withRel(() -> "get all gift-certificates by name or by part of name"));
    }


    private GiftCertificate getLinksForGiftCertificateReadAndCreateWithOitGettingByPartOfName(GiftCertificate giftCertificate) {
        return giftCertificate
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readById(giftCertificate.getId()))
                        .withRel(() -> "get gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .delete(giftCertificate.getId()))
                        .withRel(() -> "delete gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateCertificate(new GiftCertificate(), giftCertificate.getId()))
                        .withRel(() -> "update gift-certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update gift-certificate`s price"));
    }
}