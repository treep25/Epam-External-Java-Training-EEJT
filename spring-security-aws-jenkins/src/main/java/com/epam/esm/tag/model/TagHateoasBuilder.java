package com.epam.esm.tag.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.jacoco.Generated;
import com.epam.esm.tag.controller.TagController;
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
public class TagHateoasBuilder {

    private final PagedResourcesAssembler<Tag> representationModelAssembler;

    public CollectionModel<Tag> getHateoasTagForCreating(Tag tag) {
        log.info("Building HATEOAS collection-model entity");

        return CollectionModel.of(List.of(tag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(tag.getId()))
                                .withRel(() -> "delete tag"))
                        .add(linkTo(methodOn(TagController.class)
                                .readById(tag.getId()))
                                .withRel(() -> "get tag"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .readByTagName(tag.getName(), 0, 20))
                                .withRel(() -> "get gift-certificates by tag name"))))

                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));
    }

    public PagedModel<Tag> getHateoasTagForReading(Page<Tag> allTags) {
        log.info("Building HATEOAS paged-model entity");

        PagedModel<Tag> allTagsPagedModel = representationModelAssembler
                .toModel(allTags, tag -> tag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(tag.getId()))
                                .withRel(() -> "delete tag"))
                        .add(linkTo(methodOn(TagController.class)
                                .readById(tag.getId()))
                                .withRel(() -> "get tag"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .readByTagName(tag.getName(), 0, 20))
                                .withRel(() -> "get gift-certificates by tag name")));

        allTagsPagedModel.add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));

        return allTagsPagedModel;
    }

    public CollectionModel<Tag> getHateoasTagForReadingById(Tag tag) {
        log.info("Building HATEOAS collection-model entity");

        return CollectionModel.of(List.of(tag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(tag.getId()))
                                .withRel(() -> "delete tag"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .readByTagName(tag.getName(), 0, 20))
                                .withRel(() -> "get gift-certificates by tag name"))))


                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));

    }

    public CollectionModel<Tag> getHateoasTagForGettingTheMostWidelyUsedTag(Tag tag) {
        log.info("Building HATEOAS collection-model entity");
        if(tag == null){
            return CollectionModel.of(List.of());
        }
        return CollectionModel.of(List.of(tag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(tag.getId()))
                                .withRel(() -> "delete tag"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .readByTagName(tag.getName(), 0, 20))
                                .withRel(() -> "get gift-certificates by tag name"))))

                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"));
    }


}
