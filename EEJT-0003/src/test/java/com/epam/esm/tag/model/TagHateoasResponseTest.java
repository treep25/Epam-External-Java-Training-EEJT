package com.epam.esm.tag.model;

import com.epam.esm.giftcertficate.controller.GiftCertificateController;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificateHateoasResponse;
import com.epam.esm.tag.controller.TagController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class TagHateoasResponseTest {
    @InjectMocks
    private TagHateoasResponse tagHateoasResponse;
    @Mock
    private PagedResourcesAssembler<Tag> representationModelAssembler;

    @Captor
    ArgumentCaptor<Link> linkCaptor;

    @Test
    void getHateoasTagForCreatingTest() {
        //given
        Tag tagObj = Tag.builder().id(1L).name("TagPlug").build();
        Tag tagExpected = Tag.builder().id(1L).name("TagPlug").build();

        CollectionModel<Tag> expected = CollectionModel.of(List.of(tagObj));

        tagObj
                .add(linkTo(methodOn(TagController.class)
                        .delete(tagObj.getId()))
                        .withRel(() -> "delete tag"))
                .add(linkTo(methodOn(TagController.class)
                        .readById(tagObj.getId()))
                        .withRel(() -> "get tag"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName(tagObj.getName(), 0, 20))
                        .withRel(() -> "get gift-certificates by tag name"));

        expected
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));


        //when
        CollectionModel<Tag> actual = tagHateoasResponse.getHateoasTagForCreating(tagExpected);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasTagForReadingTest() {
        //given
        Tag tag = new Tag();
        Page<Tag> tags = new PageImpl<>(List.of(tag));
        PagedModel<Tag> allTagModel = mock(PagedModel.class);

        when(allTagModel.add(ArgumentMatchers.<Link>any())).thenReturn(allTagModel);

        when(representationModelAssembler.toModel(eq(tags), ArgumentMatchers.<RepresentationModelAssembler<Tag, Tag>>any())).thenReturn(allTagModel);

        //when
        PagedModel<Tag> actual = tagHateoasResponse.getHateoasTagForReading(tags);

        //then
        assertEquals(actual, allTagModel);
        verify(actual, times(2)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value(), "create tag");
        comparingLinks(linkCaptor.getAllValues().get(1).getRel().value(), "get the most widely used tag of user`s orders");
    }

    @Test
    void getHateoasTagForReadingByIdTest() {
        //given
        Tag tagObj = Tag.builder().id(1L).name("TagPlug").build();
        Tag tagExpected = Tag.builder().id(1L).name("TagPlug").build();

        CollectionModel<Tag> expected = CollectionModel.of(List.of(tagObj));
        tagObj
                .add(linkTo(methodOn(TagController.class)
                        .delete(tagObj.getId()))
                        .withRel(() -> "delete tag"))
                .add(linkTo(methodOn(TagController.class)
                        .readById(tagObj.getId()))
                        .withRel(() -> "get tag"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName(tagObj.getName(), 0, 20))
                        .withRel(() -> "get gift-certificates by tag name"));
        expected
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));

        //when
        CollectionModel<Tag> actual = tagHateoasResponse.getHateoasTagForReadingById(tagExpected);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    @Test
    void getHateoasTagForGettingTheMostWidelyUsedTagTest() {
        //given
        Tag tagObj = Tag.builder().id(1L).name("TagPlug").build();
        Tag tagExpected = Tag.builder().id(1L).name("TagPlug").build();

        CollectionModel<Tag> expected = CollectionModel.of(List.of(tagObj));

        tagObj
                .add(linkTo(methodOn(TagController.class)
                        .delete(tagObj.getId()))
                        .withRel(() -> "delete tag"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName(tagObj.getName(), 0, 20))
                        .withRel(() -> "get gift-certificates by tag name"));

        expected
                .add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"));


        //when
        CollectionModel<Tag> actual = tagHateoasResponse.getHateoasTagForGettingTheMostWidelyUsedTag(tagExpected);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    private void comparingLinks(String link1, String link2) {
        Assertions.assertEquals(link1, link2);
    }

}