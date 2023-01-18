package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;
    private final PagedResourcesAssembler<Tag> representationModelAssembler;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        if (DataValidation.isValidTag(tag)) {

            Tag savedTag = tagService.createTag(tag);

            CollectionModel<Tag> collectionModelSavedTag = CollectionModel.of(List.of(savedTag
                            .add(linkTo(methodOn(TagController.class)
                                    .delete(savedTag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(savedTag.getId()))
                                    .withRel(() -> "get tag"))))

                    .add(linkTo(methodOn(TagController.class)
                            .read(0, 20))
                            .withRel(() -> "get all tags"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTheMostWidelyUsedTag())
                            .withRel(() -> "get the most widely used tag of user`s orders"));

            return new ResponseEntity<>(Map.of("saved tag", collectionModelSavedTag), HttpStatus.CREATED);
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        PagedModel<Tag> allTags = representationModelAssembler
                .toModel(tagService.getAllTags(page, size), tag -> tag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(tag.getId()))
                                .withRel(() -> "delete tag"))
                        .add(linkTo(methodOn(TagController.class)
                                .readById(tag.getId()))
                                .withRel(() -> "get tag")));

        allTags.add(linkTo(methodOn(TagController.class)
                        .create(new Tag()))
                        .withRel(() -> "create tag"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostWidelyUsedTag())
                        .withRel(() -> "get the most widely used tag of user`s orders"));

        return ResponseEntity.ok(Map.of("all tags", allTags));
    }

    //TODO "rel": "get gift-certificates by tag name",
    //                "href": "http://localhost:8080/certificates/search/tag-name?name=&page=0&size=20"
    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            Tag currentTag = tagService.getTagById(id);

            CollectionModel<Tag> collectionModelCurrentTag = CollectionModel.of(List.of(currentTag
                            .add(linkTo(methodOn(TagController.class)
                                    .delete(currentTag.getId()))
                                    .withRel(() -> "delete tag"))))

                    .add(linkTo(methodOn(TagController.class)
                            .read(0, 20))
                            .withRel(() -> "get all tags"))
                    .add(linkTo(methodOn(TagController.class)
                            .getTheMostWidelyUsedTag())
                            .withRel(() -> "get the most widely used tag of user`s orders"));

            return ResponseEntity.ok(Map.of("tag", collectionModelCurrentTag));
        }
        throw new ServerException("tag id is not valid (id = " + id + ")");
    }

    @GetMapping("/widely-used-tag")
    public ResponseEntity<?> getTheMostWidelyUsedTag() {
        Tag theMostWidelyUsedTag = tagService.getTheMostWidelyUsedTagOfUserOrder();

        CollectionModel<Tag> collectionModelTheMostWidelyUsed = CollectionModel.of(List.of(theMostWidelyUsedTag
                        .add(linkTo(methodOn(TagController.class)
                                .delete(theMostWidelyUsedTag.getId()))
                                .withRel(() -> "delete tag"))))

                .add(linkTo(methodOn(TagController.class)
                        .read(0, 20))
                        .withRel(() -> "get all tags"));

        return ResponseEntity.ok(Map.of("the most widely used tag", collectionModelTheMostWidelyUsed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        }
        throw new ServerException("The Tag ID is not valid: id = " + id);
    }
}
